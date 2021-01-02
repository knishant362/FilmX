package com.trendster.filmx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.trendster.filmx.model.MovieDetails
import com.trendster.filmx.repository.MovieDataRepository
import com.trendster.filmx.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository : MovieDataRepository , movieId : Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchDetail(compositeDisposable , movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.fetchNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}