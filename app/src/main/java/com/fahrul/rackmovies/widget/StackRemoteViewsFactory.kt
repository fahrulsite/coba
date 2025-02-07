package com.fahrul.rackmovies.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.fahrul.rackmovies.Helper
import com.fahrul.rackmovies.R
import com.fahrul.rackmovies.lokal.Movie
import com.fahrul.rackmovies.lokal.FavoriteDb
import com.fahrul.rackmovies.lokal.TV
import com.fahrul.rackmovies.widget.AppWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StackRemoteViewsFactory(val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val favDb = FavoriteDb.getInstance(context)
    private var mWidgetItems = ArrayList<String>()
    private var favMovie = ArrayList<Movie>()
    private var favTV = ArrayList<TV>()


    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        Log.d("stackWidget", "data onchange")

        if(favMovie.size != 0 && mWidgetItems.size != 0 && favTV.size !=0){
            favMovie= java.util.ArrayList()
            favTV = java.util.ArrayList()
            mWidgetItems = java.util.ArrayList()
        }

        val identityToken = Binder.clearCallingIdentity()
        favMovie.addAll(favDb!!.movieDao().getMovies())
        favTV.addAll(favDb!!.tvShowDao().getTvShow())
        Binder.restoreCallingIdentity(identityToken)


        for (item in favMovie){
            mWidgetItems.add(Helper.POSTER_URL+"${item.poster_path}")
        }
        for (item in favTV){
            mWidgetItems.add(Helper.POSTER_URL+"${item.poster_path}")
        }

        Log.d("broadcast", "favmovie $favMovie")
        Log.d("broadcast", "favtv $favTV")
        Log.d("broadcast", "mwidget $mWidgetItems")

    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(
            context.packageName,
            R.layout.item_widget
        )

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(mWidgetItems[position])
            .submit(200, 250)
            .get()

        rv.setImageViewBitmap(R.id.imgView, bitmap)

        val intent = Intent().putExtra(AppWidget.EXTRA_ITEM, mWidgetItems[position])

        rv.setOnClickFillInIntent(R.id.imgView, intent)
        return rv
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

}