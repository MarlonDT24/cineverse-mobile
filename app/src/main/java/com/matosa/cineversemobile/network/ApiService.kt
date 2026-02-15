package com.matosa.cineversemobile.network

import com.matosa.cineversemobile.model.BookingRequest
import com.matosa.cineversemobile.model.ChatMessage
import com.matosa.cineversemobile.model.LoginRequest
import com.matosa.cineversemobile.model.LoginResponse
import com.matosa.cineversemobile.model.Movie
import com.matosa.cineversemobile.model.Session
import com.matosa.cineversemobile.model.Seat
import com.matosa.cineversemobile.model.SendMessageRequest
import com.matosa.cineversemobile.model.ConversationDTO
import com.matosa.cineversemobile.model.StarWarsEvent

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("bookings")
    suspend fun createBooking(@Body request: BookingRequest): Response<Void>

    @POST("chat/send")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<Void>

    @POST("chat/conversations")
    suspend fun createConversation(@Body body: Map<String, Int>): Response<ConversationDTO>
    @GET("movies")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("sessions")
    suspend fun getSessions(@Query("movie_id") movieId: Int): Response<List<Session>>
    @GET("sessions/{id}/seats")
    suspend fun getSeats(@Path("id") sessionId: Int): Response<List<Seat>>
    @GET("chat/history")
    suspend fun getChatHistory(@Query("user_id") userId: Int): Response<List<ChatMessage>>
    @GET("marketing/starwars")
    suspend fun getActiveEvents(): Response<List<StarWarsEvent>>
}