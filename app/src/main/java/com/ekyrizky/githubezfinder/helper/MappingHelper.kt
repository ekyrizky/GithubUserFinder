package com.ekyrizky.githubezfinder.helper

import android.database.Cursor
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.BIO
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.EMAIL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.URL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion._ID

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserEntity> {
        val favoritesList = ArrayList<UserEntity>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val bio = getString(getColumnIndexOrThrow(BIO))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val email = getString(getColumnIndexOrThrow(EMAIL))
                val url = getString(getColumnIndexOrThrow(URL))
                val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
                val following = getInt(getColumnIndexOrThrow(FOLLOWING))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))

                favoritesList.add(UserEntity(id, username, name, avatar, bio, company, location, email,
                        url, repository, following, followers))
            }
        }
        return favoritesList
    }
}