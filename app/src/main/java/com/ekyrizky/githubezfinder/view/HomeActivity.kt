package com.ekyrizky.githubezfinder.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.adapter.UserAdapter
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.ActivityHomeBinding
import com.ekyrizky.githubezfinder.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var adapter: UserAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var listUser: ArrayList<UserEntity> = ArrayList()

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        setSupportActionBar(homeBinding.toolbar)

        adapter = UserAdapter(listUser)

        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(HomeViewModel::class.java)

        searchUser()
        setUserData()
        getUserData(adapter)
        showRecyclerView()

    }

    private fun getUserData(userAdapter: UserAdapter) {
        homeViewModel.getUsers().observe(this, {userItems ->
            if (userItems != null) {
                userAdapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun searchUser() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        homeBinding.search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        homeBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    listUser.clear()
                    showLoading(true)
                    homeViewModel.getUserSearch(query, applicationContext)
                    homeBinding.search.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) setUserData()
                return true
            }

        })
    }

    private fun setUserData() {
        homeViewModel.setUser(applicationContext)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            homeBinding.progressBar.visibility = View.VISIBLE
        } else {
            homeBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        homeBinding.rvUser.layoutManager = LinearLayoutManager(this)
        homeBinding.rvUser.setHasFixedSize(true)
        homeBinding.rvUser.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> {
                val moveIntent = Intent(this, UserFavoriteActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.setting -> {
                val moveIntent = Intent(this, SettingActivity::class.java)
                startActivity(moveIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}