package com.fahrul.rackmovies.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahrul.rackmovies.Helper
import com.fahrul.rackmovies.api.ApiClient
import com.fahrul.rackmovies.lokal.FavoriteDb
import com.fahrul.rackmovies.lokal.Movie
import com.fahrul.rackmovies.lokal.TV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(val context: Context, val id: String) : ViewModel() {
    companion object {
        private val TAG = DataViewModel::class.java.simpleName
    }

    private val service = ApiClient.create()
    private val apiKey = Helper.API_KEY

    private val favDb = FavoriteDb.getInstance(context)

    private val movieDetail = MutableLiveData<Movie>()
    private val tvDetail = MutableLiveData<TV>()
    private var isFavorite = MutableLiveData<Boolean>()

    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()

    private fun setMovieDetail() {
        isLoading.postValue(true)

        service.getMovieDetail(id, apiKey).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val movie = response.body()

                    movieDetail.postValue(movie)
                }
            }
        })
    }

    private fun setTvShowDetail() {
        isLoading.postValue(true)

        service.getTvDetail(id, apiKey).enqueue(object : Callback<TV> {
            override fun onFailure(call: Call<TV>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<TV>, response: Response<TV>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val tvShow = response.body()

                    tvDetail.postValue(tvShow)
                }
            }
        })
    }

    internal fun checkIsFavoriteMovie(id: String): LiveData<Boolean> {
        GlobalScope.launch {
            if (favDb?.movieDao()?.getMovie(id) != null) {
                isFavorite.postValue(true)
            }
        }

        return isFavorite
    }

    internal fun checkIsFavoriteTv(id: String): LiveData<Boolean> {
        GlobalScope.launch {
            if (favDb?.tvShowDao()?.getTvShow(id) != null) {
                isFavorite.postValue(true)
            }
        }

        return isFavorite
    }

    internal fun setFavoriteMovie(movie: Movie) {
        GlobalScope.launch {
            favDb?.movieDao()?.insertMovie(movie)
            isFavorite.postValue(true)
        }
    }

    internal fun setFavoriteTv(tvShow: TV) {
        GlobalScope.launch {
            favDb?.tvShowDao()?.insertTvShow(tvShow)
            isFavorite.postValue(true)
        }
    }

    internal fun deleteFavoriteMovie(id: String) {
        GlobalScope.launch {
            favDb?.movieDao()?.deleteMovie(id)
            isFavorite.postValue(false)
        }
    }

    internal fun deleteFavoriteTv(id: String) {
        GlobalScope.launch {
            favDb?.tvShowDao()?.deleteTvShow(id)
            isFavorite.postValue(false)
        }
    }

    internal fun getMovieDetail(): MutableLiveData<Movie> {
        if (movieDetail.value == null) {
            setMovieDetail()
        }
        return movieDetail
    }

    internal fun getTvDetail(): MutableLiveData<TV> {
        if (tvDetail.value == null) {
            setTvShowDetail()
        }
        return tvDetail
    }
}