package com.fahrul.rackmovies.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.fahrul.rackmovies.lokal.FavoriteDb
import com.fahrul.rackmovies.Helper

class FavoriteProvider : ContentProvider() {
    companion object {
        private val AUTHORITY = "com.fahrul.rackmovies"
        private var favDb: FavoriteDb? = null

        private const val MOVIE = 10
        private const val MOVIE_ID = 11

        private const val TV = 20
        private const val TV_ID = 21

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, Helper.TABLE_MOVIE, MOVIE)
            uriMatcher.addURI(AUTHORITY, "${Helper.TABLE_MOVIE}/#", MOVIE_ID)

            uriMatcher.addURI(AUTHORITY, Helper.TABLE_TV_SHOW, TV)
            uriMatcher.addURI(AUTHORITY, "${Helper.TABLE_TV_SHOW}/#", TV_ID)
        }
    }

    override fun onCreate(): Boolean {
        favDb = FavoriteDb.getInstance(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (uriMatcher.match(uri)) {
            MOVIE -> {
                Log.d("myProvider", "getting query")
                favDb?.movieDao()?.getMoviesCursor()
            }

            MOVIE_ID -> favDb?.movieDao()?.getMoviesCursor(
                ContentUris.parseId(uri).toString()
            )

            TV -> favDb?.tvShowDao()?.getTvShowCursor()

            TV_ID -> favDb?.tvShowDao()?.getTvShowCursor(
                ContentUris.parseId(uri).toString()
            )

            else -> {
                Log.d("myProvider", "failed getting query")
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}