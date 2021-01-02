package com.trendster.filmx.ui.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.api.POST_PER_PAGE
import com.trendster.filmx.model.Movie
import com.trendster.filmx.repository.MovieListDataSource
import com.trendster.filmx.repository.MovieListDataSourceFactory
import com.trendster.filmx.util.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviesPagesListRepository (private val apiService: ApiService) {

    lateinit var moviesPagedList : LiveData<PagedList<Movie>>
    lateinit var movieListDataSourceFactory : MovieListDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {

        movieListDataSourceFactory = MovieListDataSourceFactory(apiService , compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviesPagedList = LivePagedListBuilder(movieListDataSourceFactory , config).build()

        return moviesPagedList
    }

    fun getNetworkState () : LiveData<NetworkState> {
        return Transformations.switchMap<MovieListDataSource , NetworkState> (
            movieListDataSourceFactory.moviesLiveDataSource , MovieListDataSource::networkState
        )
    }

}