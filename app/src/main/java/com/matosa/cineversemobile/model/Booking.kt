package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Booking(
    val id: Int,

    @SerializedName("booking_code")
    val bookingCode: String,

    @SerializedName("total_amount")
    val totalAmount: Double,

    val status: String,

    @SerializedName("created_at")
    val createdAt: String,

    val session: Session?,

    val movie: Movie?
) : Serializable