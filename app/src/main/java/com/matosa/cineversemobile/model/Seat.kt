package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Seat(
    val id: Int,
    @SerializedName("row_letter")
    val row: String,
    @SerializedName("seat_number")
    val number: Int,
    @SerializedName("seat_type")
    val type: String,
    var isBooked: Boolean = false,
    var isSelected: Boolean = false,
    var price: Double = 0.0
) : Serializable