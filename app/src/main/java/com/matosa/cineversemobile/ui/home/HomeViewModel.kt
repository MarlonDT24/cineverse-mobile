package com.matosa.cineversemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matosa.cineversemobile.model.Movie
import com.matosa.cineversemobile.model.StarWarsEvent
import com.matosa.cineversemobile.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _starWarsEvents = MutableLiveData<List<StarWarsEvent>>()
    val starWarsEvents: LiveData<List<StarWarsEvent>> get() = _starWarsEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Al iniciarse, cargamos las películas automáticamente
    init {
        fetchMovies()
    }

    fun fetchMovies() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val moviesResponse = RetrofitClient.instance.getMovies()
                val eventsResponse = RetrofitClient.instance.getActiveEvents()

                if (moviesResponse.isSuccessful) _movies.value = moviesResponse.body()

                if (eventsResponse.isSuccessful) {
                    _starWarsEvents.value = eventsResponse.body()
                }
            } catch (e: Exception) {
                _error.value = "Fallo de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}