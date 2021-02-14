package com.ekyrizky.githubezfinder.view

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.adapter.SectionPageAdapter
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.ActivityUserDetailBinding
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.BIO
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.EMAIL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.URL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion._ID
import com.ekyrizky.githubezfinder.db.FavoriteHelper
import com.google.android.material.tabs.TabLayout

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var userDetailBinding: ActivityUserDetailBinding
    private  lateinit var favUser: UserEntity
    private lateinit var uriWithId: Uri
    private var isFavorite = false

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(userDetailBinding.root)
        setSupportActionBar(userDetailBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp)

        favoriteState()
        setData()
        showViewPager()
        userDetailBinding.fabFavorite.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_favorite) {
            if (!isFavorite) {
                val values = ContentValues()
                values.put(_ID, favUser.id)
                values.put(USERNAME, favUser.username)
                values.put(NAME, favUser.name)
                values.put(AVATAR, favUser.avatar)
                values.put(BIO, favUser.bio)
                values.put(COMPANY, favUser.company)
                values.put(LOCATION, favUser.location)
                values.put(EMAIL, favUser.email)
                values.put(URL, favUser.url)
                values.put(REPOSITORY, favUser.repository)
                values.put(FOLLOWING, favUser.following)
                values.put(FOLLOWERS, favUser.followers)
                contentResolver.insert(CONTENT_URI, values)

                Toast.makeText(this, getString(R.string.add_fav, favUser.username),
                        Toast.LENGTH_SHORT).show()
                Log.d(HomeActivity.TAG, "insert : $values")
                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)

            } else {
                uriWithId = Uri.parse("$CONTENT_URI/${favUser.id}")
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this, getString(R.string.delete_fav, favUser.username),
                        Toast.LENGTH_SHORT).show()
                Log.d(HomeActivity.TAG, "delete: ${favUser.username}")
                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)
            }
        }
    }

    private fun favoriteState() {
        val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favUser = intent.getParcelableExtra<UserEntity>(EXTRA_USER) as UserEntity


        val cursor: Cursor = favoriteHelper.queryById(favUser.id.toString())
        if (cursor.moveToNext()) {
            isFavorite = true
            setStatusFavorite(isFavorite)
        }
    }

    private fun showViewPager() {
        val sectionPageAdapter = SectionPageAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = userDetailBinding.viewPager
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = userDetailBinding.tabLayout
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
    }

    private fun setData() {
        val user = intent.getParcelableExtra<UserEntity>(EXTRA_USER) as UserEntity

        Glide.with(this)
            .load(user.avatar)
            .placeholder(R.drawable.profile_black_96dp)
            .error(R.drawable.profile_black_96dp)
            .into(userDetailBinding.imgAvatar)
        userDetailBinding.toolbar.title = getString(R.string.profile, user.username)
        userDetailBinding.tvUsername.text = user.username
        userDetailBinding.tvName.text = user.name
        userDetailBinding.tvBio.text = user.bio
        userDetailBinding.tvCompany.text = user.company
        userDetailBinding.tvLocation.text = user.location
        userDetailBinding.tvEmail.text = user.email
        userDetailBinding.tvUrl.text = user.url
        userDetailBinding.tvRepository.text = user.repository.toString()
        userDetailBinding.tvFollowing.text = getString(R.string.following, user.following)
        userDetailBinding.tvFollowers.text = getString(R.string.followers, user.followers)

        userDetailBinding.tvName.visibility = if (user.name == "null") View.GONE else View.VISIBLE
        userDetailBinding.tvBio.visibility = if (user.bio == "null") View.GONE else View.VISIBLE
        userDetailBinding.tvCompany.visibility = if (user.company == "null") View.GONE else View.VISIBLE
        userDetailBinding.tvLocation.visibility = if (user.location == "null") View.GONE else View.VISIBLE
        userDetailBinding.tvEmail.visibility = if (user.email == "null") View.GONE else View.VISIBLE
    }

    private fun setStatusFavorite(status: Boolean) {
        if (status) {
            userDetailBinding.fabFavorite.setImageResource(R.drawable.favorite_black_24dp)
        } else {
            userDetailBinding.fabFavorite.setImageResource(R.drawable.favorite_border_black_24dp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                val shareObject = userDetailBinding.tvUrl.text
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "type/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareObject)
                startActivity(Intent.createChooser(shareIntent, null))
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}