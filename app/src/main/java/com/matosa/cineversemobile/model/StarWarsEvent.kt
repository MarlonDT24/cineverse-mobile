package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName

data class StarWarsEvent(
    val id: Int,
    @SerializedName("swapi_id")
    val swapiId: Int,
    val title: String,
    val description: String,
    @SerializedName("characterName")
    val characterName: String,
    @SerializedName("event_date")
    val eventDate: String,
    val active: Int
)z