package com.fadhlansulistiyo.codingstoryapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import com.fadhlansulistiyo.codingstoryapp.di.Injection
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }
}

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var storyItems: List<ListStoryItem> = emptyList()

    override fun onCreate() {
        val repository = Injection.provideRepository(context)
        runBlocking {
            storyItems = repository.getStories().listStory
        }
    }

    override fun onDataSetChanged() {
        val repository = Injection.provideRepository(context)
        runBlocking {
            storyItems = repository.getStories().listStory
        }
    }

    override fun onDestroy() {
        // Cleanup
    }

    override fun getCount(): Int {
        return storyItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_item)
        val item = storyItems[position]

        item.photoUrl?.let { photoUrl ->
            val bitmap = loadImageFromUrl(photoUrl)
            views.setImageViewBitmap(R.id.widget_item_image, bitmap)
        }

        return views
    }

    private fun loadImageFromUrl(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
