package com.trendster.filmx.ui.movieslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trendster.filmx.R
import com.trendster.filmx.api.POSTER_BASE_URL
import com.trendster.filmx.model.Movie
import com.trendster.filmx.ui.singlemovie.SingleMovie
import com.trendster.filmx.util.NetworkState

class MovieListAdapter (val context: Context) : PagedListAdapter<Movie , RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.post_row , parent , false)
            return MovieItemViewHolder(view)
        }
        else {
            view = layoutInflater.inflate(R.layout.network_state_item , parent , false)
            return NetworkStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context )
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }

    }

    class MovieItemViewHolder (view : View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie? , context : Context) {
            val title : TextView = itemView.findViewById(R.id.cv_movie_title)
            val releaseDate : TextView = itemView.findViewById(R.id.cv_movie_release_date)

            title.text = movie?.title
            releaseDate.text = movie?.releaseDate

            val moviePoster : ImageView = itemView.findViewById(R.id.cv_iv_movie_poster)
            val url = POSTER_BASE_URL + movie?.posterPath

            Glide.with(itemView.context)
                .load(url)
                .into(moviePoster)

            itemView.setOnClickListener {
                val intent = Intent(context , SingleMovie::class.java)
                intent.putExtra("id" , movie?.id)
                context.startActivity(intent)
            }
        }
    }

    private fun hasExtraRow () : Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if( hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class NetworkStateItemViewHolder (view : View) : RecyclerView.ViewHolder (view) {
        val progressBar : ProgressBar = itemView.findViewById( R.id.progress_bar_item)
        val errorMsg : TextView = itemView.findViewById(R.id.error_msg_item)

        fun bind (networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING ) {

                progressBar.visibility = View.VISIBLE
            }
            else {
                progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.msg
            }
            else {
                errorMsg.visibility = View.GONE
            }

        }

    }
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem ==newItem
        }

    }

    fun setNetworkState (newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()



        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())

            }
        }else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }




}