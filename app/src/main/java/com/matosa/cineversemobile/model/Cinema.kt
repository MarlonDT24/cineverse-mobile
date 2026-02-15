package com.matosa.cineversemobile.model

import java.io.Serializable

data class Cinema(
    val id: Int,
    val name: String?,
    val totalSeats: Int?
) : Serializable