package com.ekyrizky.myfavorite

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekyrizky.myfavorite.adapter.FavoriteAdapter
import com.ekyrizky.myfavorite.data.UserEntity
import com.ekyrizky.myfavorite.databinding.ActivityMainBinding
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ekyrizky.myfavorite.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        setSupportActionBar(favoriteBinding.toolbar)

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

    private fun showSnackBar() {
        Snackbar.make(favoriteBinding.rvFavorite, "Favorite User is Empty", Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            favoriteBinding.progressBar.visibility = View.VISIBLE
        } else {
            favoriteBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        favoriteBinding.rvFavorite.layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter()
        favoriteBinding.rvFavorite.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}