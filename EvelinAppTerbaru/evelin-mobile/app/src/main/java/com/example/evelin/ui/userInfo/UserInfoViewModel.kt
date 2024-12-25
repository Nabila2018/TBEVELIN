package com.example.evelin.ui.userInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.LoginUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserInfoViewModel (private val repository: UserRepository) : ViewModel() {
    private val _user = MutableStateFlow<LoginUser?>(null)
    val user: StateFlow<LoginUser?> = _user.asStateFlow()


    fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = repository.getUser()

                _user.value = response
                Log.d("RegisterEventViewModel", "User data fetched: $response")
            } catch (e: Exception) {
                _user.value = null
                // Log error or handle appropriately
            }
        }
    }
}