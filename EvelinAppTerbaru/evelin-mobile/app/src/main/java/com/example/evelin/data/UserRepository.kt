package com.example.evelin.data

import android.util.Log
import com.example.evelin.data.pref.UserModel
import com.example.evelin.data.pref.UserPreference

import com.example.evelin.data.remote.ApiService
import com.example.evelin.data.request.LoginRequest
import com.example.evelin.data.request.RegisterRequest
import com.example.evelin.data.request.UpdateEventRequest
import com.example.evelin.data.response.AddEventResponse
import com.example.evelin.data.response.DataUser
import com.example.evelin.data.response.EventResponse
import com.example.evelin.data.response.EventsResponse
import com.example.evelin.data.response.HistoryResponse
import com.example.evelin.data.response.LoginResponse
import com.example.evelin.data.response.LoginUser
import com.example.evelin.data.response.RegisterEventResponse
import com.example.evelin.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email,password))
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    private fun getToken(): String {
        val user = runBlocking { userPreference.getSession().first() }
        Log.d("UserRepository", "Token: ${user.token}")
        return runBlocking { userPreference.getSession().first().token }
    }


    suspend fun register(request: RegisterRequest): RegisterResponse {
        return try {
            apiService.register(request)
        } catch (e: HttpException) {
            throw e
        }
    }


    suspend fun getUser(): LoginUser {
        val token = getToken()
        try {
            val dataUser = apiService.getUser("Bearer $token").data
            Log.d("UserRepository", "User data: $dataUser")
            return apiService.getUser("Bearer $token").data
        } catch (e: HttpException) {
            if (e.code() == 401) {
                logout()
                throw e
            } else {
                throw e
            }
        }
    }




    suspend fun getEvents(): EventsResponse {
        val token = getToken()
        try {
            return apiService.getEvents("Bearer $token")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                logout()
                throw e
            } else {
                throw e
            }
        }
    }
//
    suspend fun getEvent(id: String): EventResponse {
        val token = getToken()
        Log.d("UserRepository", "Fetching event with id: $id and token: $token")
        return try {
            apiService.getEvent("Bearer $token", id)
        } catch (e: HttpException) {
            Log.e("UserRepository", "HTTP error: ${e.code()} - ${e.message()}")
            throw e
        }
    }

    suspend fun updateEvent(
        eventId: String,
        eventDate: String,
        location: String
    ): EventResponse {
        val token = getToken()
        return apiService.updateEvent("Bearer $token", eventId, UpdateEventRequest(eventDate, location))    }

    suspend fun getHistory(): HistoryResponse {
        val token = getToken()
        try {
            return apiService.getHistory("Bearer $token")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                logout()
                throw e
            } else {
                throw e
            }
        }
    }

    suspend fun searchEvents(token: String, query: String): EventsResponse {
        return apiService.searchEvents(token, query)
    }


    suspend fun getMyEvent(): EventsResponse {
        val token = getToken()
        try {
            return apiService.getMyEvent("Bearer $token")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                logout()
                throw e
            } else {
                throw e
            }
        }
    }


    suspend fun submitEventRegistration(eventId: String): RegisterEventResponse {
        val token = getToken()
        return apiService.registerEvent("Bearer $token", eventId)
    }

    suspend fun addEvent(
        title: RequestBody,
        description: RequestBody?,
        eventDate: RequestBody?,
        location: RequestBody?,
        category: RequestBody?,
        posterUrl: MultipartBody.Part
    ): AddEventResponse {
        val token = getToken() // Assume you have a method to get the stored token
        return apiService.addEvent("Bearer $token", title, description, eventDate, location, category, posterUrl)
    }
//
//    suspend fun addMeasure(
//        baby_photo_url: MultipartBody.Part,
//        levelActivity: RequestBody,
//        statusAsi: RequestBody?,
//        age: RequestBody?,
//        weight: RequestBody?,
//        date: RequestBody?
//    ): AddMeasureResponse {
//        val token = getToken()
//
//        return apiService.addMeasure("Bearer $token", baby_photo_url, levelActivity, statusAsi, age, weight, date)
//    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}