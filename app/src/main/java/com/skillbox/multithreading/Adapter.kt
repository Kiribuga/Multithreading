package com.skillbox.multithreading

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import com.skillbox.multithreading.networking.Movie

class AdapterMovie() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movies: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieHolder(parent.inflate(R.layout.item_movie))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieHolder -> {
                val movie = movies[position]
                holder.bind(movie)
            }
            else -> error("Incorrect view holder = $holder")
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateListMovies(newList: List<Movie>) {
        movies = newList
    }

    abstract class BaseHolder(
        final override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val titleMovie: TextView = containerView.findViewById(R.id.titleMovie)
        private val yearMovie: TextView = containerView.findViewById(R.id.yearMovie)

        protected fun bindMainInfo(
            title: String,
            year: Int
        ) {
            titleMovie.text = title
            yearMovie.text = year.toString()
        }
    }

    class MovieHolder(
        containerView: View
    ) : BaseHolder(containerView) {

        fun bind(movie: Movie) {
            bindMainInfo(movie.title, movie.year)
        }
    }
}