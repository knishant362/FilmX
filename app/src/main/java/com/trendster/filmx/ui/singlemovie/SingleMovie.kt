package com.trendster.filmx.ui.singlemovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.trendster.filmx.R
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.api.MovieClient
import com.trendster.filmx.model.MovieDetails
import com.trendster.filmx.repository.MovieDataRepository
import com.trendster.filmx.util.NetworkState
import com.trendster.filmx.viewmodel.SingleMovieViewModel

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel : SingleMovieViewModel
    private lateinit var movieRepository: MovieDataRepository

    private lateinit var movieTitle : TextView
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        movieTitle = findViewById(R.id.movie_title)
        progressBar = findViewById(R.id.progress_bar)

        val movieId : Int = 299534

        val apiService : ApiService = MovieClient.getClient()
        movieRepository = MovieDataRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this , Observer {
            bindUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE

        })

    }

    private fun bindUi(it: MovieDetails?) {

        Log.d("Title" , it?.title!!)
        movieTitle.text = it.title

    }

    private fun getViewModel (movieId : Int) : SingleMovieViewModel {
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository , movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

}