package com.skillbox.multithreading

import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network
import java.util.*

class Repository {

    fun fetchMovies(
        movieIds: List<String>,
        onMoviesFetched: (movies: List<Movie>) -> Unit
    ) {
        Thread {
            val allMovies = Collections.synchronizedList(mutableListOf<Movie>())

            val threads = movieIds.chunked(1).map { movieChunk ->
                Thread {
                    val movies = movieChunk.mapNotNull { movieId ->
                        Network.getMovieById(movieId)
                    }
                    allMovies.addAll(movies)
                }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
            onMoviesFetched(allMovies)
        }.start()
    }

}