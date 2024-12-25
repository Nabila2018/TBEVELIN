package com.example.evelin.data.remote

import com.example.evelin.data.request.LoginRequest
import com.example.evelin.data.request.RegisterRequest
import com.example.evelin.data.request.UpdateEventRequest
import com.example.evelin.data.response.AddEventResponse
import com.example.evelin.data.response.EventResponse
import com.example.evelin.data.response.EventsResponse
import com.example.evelin.data.response.HistoryResponse
import com.example.evelin.data.response.LoginResponse
import com.example.evelin.data.response.RegisterEventResponse
import com.example.evelin.data.response.RegisterResponse
import com.example.evelin.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserResponse

    @GET("/events")
    suspend fun getEvents(
        @Header("Authorization") token: String
    ): EventsResponse

    @GET("/event/{id}")
    suspend fun getEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): EventResponse



    @POST("/regEvent/{id}")
    suspend fun registerEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): RegisterEventResponse

    @Multipart
    @POST("/addEvent")
    suspend fun addEvent(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("eventDate") eventDate: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part posterUrl: MultipartBody.Part
    ): AddEventResponse


    @GET("/history")
    suspend fun getHistory(
        @Header("Authorization") token: String
    ): HistoryResponse



    @GET("/myEvents")
    suspend fun getMyEvent(
        @Header("Authorization") token: String
    ): EventsResponse

    @PUT("/editEvent/{id}")
    suspend fun updateEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: String,
        @Body request: UpdateEventRequest
    ): EventResponse

    @GET("/searchEvent")
    suspend fun searchEvents(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): EventsResponse

}

