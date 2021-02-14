package com.ekyrizky.githubezfinder.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ekyrizky.githubezfinder.helper.MappingHelper
import java.util.concurrent.ExecutionException

class StackRemoteViewsFactory(private val context: Context) :
        RemoteViewsService.RemoteViewsFactory {

    private var items = ArrayList<UserEntity>()
    private var cursor: Cursor? = null

    override fun onCreate() {}

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver?.query(CONTENT_URI, null, null, null, null)
        val list = MappingHelper.mapCursorToArrayList(cursor)
        items.addAll(list)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = items.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        try {
            val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(items[position].avatar)
                    .submit()
                    .get()
            rv.setImageViewBitmap(R.id.imageView, bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}