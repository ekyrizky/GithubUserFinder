package com.ekyrizky.myfavorite.helper

import android.database.Cursor
import com.ekyrizky.myfavorite.data.UserEntity
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.BIO
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.EMAIL
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.URL
import com.ekyrizky.myfavorite.db.DatabaseContract.FavoriteColumns.Companion.USERNAME

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserEntity> {
        val favoritesList = ArrayList<UserEntity>()

        cursor?.apply {
            while (moveToNext()) {
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

                favoritesList.add(UserEntity(username, name, avatar, bio, company, location, email,
                        url, repository, following, followers))
            }
        }
        return favoritesList
    }
}