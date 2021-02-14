package com.ekyrizky.githubezfinder.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.BIO
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.EMAIL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.URL
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbuserfavorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY," +
                " $USERNAME TEXT NOT NULL, " +
                " $NAME TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL," +
                " $BIO TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $EMAIL TEXT NOT NULL," +
                " $URL TEXT NOT NULL,"+
                " $REPOSITORY TEXT NOT NULL," +
                " $FOLLOWERS TEXT NOT NULL," +
                " $FOLLOWING TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}