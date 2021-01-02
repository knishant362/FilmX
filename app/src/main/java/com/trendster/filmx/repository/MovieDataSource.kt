package com.trendster.filmx.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.model.MovieDetails
import com.trendster.filmx.util.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource ( private val apiService: ApiService , private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState>
        get() = _networkState

    private val _movieData = MutableLiveData<MovieDetails>()
    val movieData : LiveData<MovieDetails>
        get() = _movieData

    fun fetchMovie (movieId : Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _movieData.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.d("MovieDataSource" , it.message!!)
                        }
                    )
            )
        }catch (e : Exception) {
            Log.d("MovieDataSource" , e.message!!)
        }
    }

}