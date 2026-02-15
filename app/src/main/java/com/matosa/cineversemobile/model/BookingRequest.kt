package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName

data class BookingRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("session_id")
    val sessionId: Int,

    @SerializedName("seat_ids")
    val seatIds: List<Int>,

    @SerializedName("total_price")
    val totalPrice: Double
)