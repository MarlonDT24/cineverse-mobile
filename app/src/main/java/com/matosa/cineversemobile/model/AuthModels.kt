package com.matosa.cineversemobile.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val id: Int,
    val username: String?,
    val role: String?
)