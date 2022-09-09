package com.skillbox.multithreading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.multithreading.networking.Movie

class ViewModelMovie : ViewModel() {

    private val movieId = listOf(
        "tt0111161", "tt0068646", "tt0468569", "tt0108052", "tt0167260"
    )

    private val repository = Repository()

    private val movieLiveData = MutableLiveData<List<Movie>>()

    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    fun requestMovie(){
        repository.fetchMovies(movieId) { movies ->
            movieLiveData.postValue(movies)
        }
    }
}