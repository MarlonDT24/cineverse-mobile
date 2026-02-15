package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName

data class ConversationDTO(
    val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userUsername") val userUsername: String?,
    @SerializedName("employeeId") val employeeId: Int?,
    val status: String,
    @SerializedName("createdAt") val createdAt: String?
)