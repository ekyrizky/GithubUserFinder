package com.ekyrizky.githubezfinder.view

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.adapter.FavoriteAdapter
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.ActivityUserFavoriteBinding
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ekyrizky.githubezfinder.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityUserFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        setSupportActionBar(favoriteBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp)

        showRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserEntity>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val favorites = deferredFavorites.await()
            if (favorites.size > 0 ) {
                adapter.listFavorite = favorites
            } else {
                adapter.listFavorite = ArrayList()
                showSnackBar()
            }
        }
    }

    private fun showSnackBar() {
        Snackbar.make(favoriteBinding.rvFavorite, "Favorite User is Empty", Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

    private fun showRecyclerView() {
        favoriteBinding.rvFavorite.layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        favoriteBinding.rvFavorite.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            favoriteBinding.progressBar.visibility = View.VISIBLE
        } else {
            favoriteBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}