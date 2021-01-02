package com.trendster.filmx.repository

import androidx.lifecycle.LiveData
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.model.MovieDetails
import com.trendster.filmx.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDataRepository ( private val apiService: ApiService) {

    lateinit var movieDataSource: MovieDataSource

    fun fetchDetail (compositeDisposable: CompositeDisposable , movieId : Int) : LiveData<MovieDetails> {
        movieDataSource = MovieDataSource(apiService, compositeDisposable)
        movieDataSource.fetchMovie(movieId)

        return movieDataSource.movieData
    }

    fun fetchNetworkState () : LiveData<NetworkState> {
        return movieDataSource.networkState
    }

}