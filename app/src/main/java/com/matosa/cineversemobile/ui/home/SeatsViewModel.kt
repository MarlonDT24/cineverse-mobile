package com.matosa.cineversemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matosa.cineversemobile.model.Seat
import com.matosa.cineversemobile.network.RetrofitClient
import kotlinx.coroutines.launch

class SeatsViewModel : ViewModel() {

    private val _seats = MutableLiveData<List<Seat>>()
    val seats: LiveData<List<Seat>> get() = _seats

    // Lista de asientos seleccionados por el usuario
    val selectedSeats = mutableListOf<Seat>()

    fun loadSeats(sessionId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getSeats(sessionId)
                if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                    _seats.value = response.body()
                } else {
                    loadMockSeats()
                }
            } catch (e: Exception) {
                loadMockSeats()
            }
        }
    }
    fun toggleSeatSelection(seat: Seat) {
        if (seat.isSelected) {
            if (!selectedSeats.contains(seat)) {
                selectedSeats.add(seat)
            }
        } else {
            selectedSeats.remove(seat)
        }
    }
    private fun loadMockSeats() {
        val currentSession = _seats.value?.firstOrNull()

        val pNormal = 8.50
        val pVip = 12.00

        val dummyList = mutableListOf<Seat>()
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        var idCounter = 1

        for (row in rows) {
            for (number in 1..8) {
                val isVip = (row == "G" || row == "H")
                val type = if (isVip) "VIP" else "NORMAL"
                val price = if (isVip) pVip else pNormal

                val isRandomlyBooked = (1..10).random() > 8

                dummyList.add(
                    Seat(
                        id = idCounter++,
                        row = row,
                        number = number,
                        type = type,
                        price = price,
                        isBooked = isRandomlyBooked,
                        isSelected = false
                    )
                )
            }
        }
        _seats.postValue(dummyList)
    }
}