package com.trendster.filmx.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieListDataSourceFactory (private val apiService: ApiService , private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, Movie> {

         val movieListDataSource1 = MovieListDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieListDataSource1)
        return movieListDataSource1

    }
}