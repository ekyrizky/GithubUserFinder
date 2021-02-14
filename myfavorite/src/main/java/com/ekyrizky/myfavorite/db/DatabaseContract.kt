package com.ekyrizky.myfavorite.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY ="com.ekyrizky.githubezfinder"
    const val SCHEME = "content"

    internal class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val BIO = "bio"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val EMAIL = "email"
            const val URL = "url"
            const val REPOSITORY = "repository"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"

            val CONTENT_URI: Uri = Uri.Builder()
                    .scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}