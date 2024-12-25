package com.example.evelin.di

import android.content.Context
import com.example.evelin.data.UserRepository
import com.example.evelin.data.pref.UserPreference
import com.example.evelin.data.pref.dataStore
import com.example.evelin.data.remote.ApiConfig


    object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(apiService, pref)

    }
}