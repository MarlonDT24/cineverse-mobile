package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val description: String?,
    val director: String?,
    val genre: String?,
    @SerializedName("duration_minutes")
    val duration: Int?,
    @SerializedName("poster_url")
    val posterUrl: String?
) : Serializable