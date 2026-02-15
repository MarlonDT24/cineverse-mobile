package com.matosa.cineversemobile.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matosa.cineversemobile.model.LoginRequest
import com.matosa.cineversemobile.model.LoginResponse
import com.matosa.cineversemobile.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(user: String, pass: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val request = LoginRequest(user, pass)
                // Llamamos a la API
                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = response.body()
                } else {
                    _error.value = "Error: Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Fallo de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}