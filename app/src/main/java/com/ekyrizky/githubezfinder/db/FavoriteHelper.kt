package com.ekyrizky.githubezfinder.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FavoriteHelper(context)
                }

        private lateinit var database: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor = database.query(
            DATABASE_TABLE, null, null, null, null, null, "$_ID ASC"
    )

    fun queryById(id: String): Cursor = database.query(
            DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null
    )

    fun insert(values: ContentValues?): Long = database.insert(
            DATABASE_TABLE, null, values
    )

    fun update(id: String, values: ContentValues?): Int = database.update(
            DATABASE_TABLE, values, "$_ID = ?", arrayOf(id)
    )

    fun deleteById(id: String): Int = database.delete(
            DATABASE_TABLE, "$_ID = '$id'", null
    )
}