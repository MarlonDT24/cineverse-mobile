package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Session(
    val id: Int,
    val movieId: Int,
    val cinema: Cinema?,
    val sessionDatetime: String,
    val priceNormal: Double,
    @SerializedName("price_vip")
    val priceVip: Double?,
    val availableSeats: Int
) : Serializable