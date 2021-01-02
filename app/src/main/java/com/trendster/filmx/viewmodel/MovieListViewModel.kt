package com.trendster.filmx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.trendster.filmx.model.Movie
import com.trendster.filmx.ui.movieslist.MoviesPagesListRepository
import com.trendster.filmx.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieListViewModel (private val moviesPagesListRepository: MoviesPagesListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        moviesPagesListRepository.fetchLiveMoviePagedList(compositeDisposable)
    }
    val networkState : LiveData<NetworkState> by lazy {
        moviesPagesListRepository.getNetworkState()
    }

    fun listIsEmpty() : Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}