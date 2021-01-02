package com.trendster.filmx.ui.movieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trendster.filmx.R
import com.trendster.filmx.api.ApiService
import com.trendster.filmx.api.MovieClient
import com.trendster.filmx.util.NetworkState
import com.trendster.filmx.viewmodel.MovieListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel
    lateinit var movieRepository : MoviesPagesListRepository

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var errorText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_movie_list)
        progressBar = findViewById(R.id.progress_bar_popular)
        errorText = findViewById(R.id.txt_error_popular)

        val apiService : ApiService = MovieClient.getClient()

        movieRepository = MoviesPagesListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = MovieListAdapter(this)
        val gridLM = GridLayoutManager(this, 3)

        gridLM.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1   /** MOVIE_VIEW_TYPE  will occupy 1 out of 3 */
                else return 3                                             /** NETWORK_VIEW_TYPE  will occupy 3 out of 3 */
            }

        }

        recyclerView.layoutManager = gridLM
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this , Observer {
            progressBar.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            errorText.visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }

        })

    }

    private fun getViewModel () :  MovieListViewModel {

        return ViewModelProviders.of(this,object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(movieRepository) as T
            }

        })[MovieListViewModel::class.java]

    }
}