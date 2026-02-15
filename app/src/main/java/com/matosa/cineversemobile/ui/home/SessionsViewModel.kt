package com.matosa.cineversemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matosa.cineversemobile.model.Session
import com.matosa.cineversemobile.network.RetrofitClient
import kotlinx.coroutines.launch

class SessionsViewModel : ViewModel() {

    private val _sessions = MutableLiveData<List<Session>>()
    val sessions: LiveData<List<Session>> get() = _sessions

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadSessions(movieId: Int) {
        viewModelScope.launch {
            try {
                // Llamamos a getSessions pasando el ID
                val response = RetrofitClient.instance.getSessions(movieId)

                if (response.isSuccessful && response.body() != null) {
                    _sessions.value = response.body()
                } else {
                    _error.value = "No hay sesiones disponibles (Error ${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }
}