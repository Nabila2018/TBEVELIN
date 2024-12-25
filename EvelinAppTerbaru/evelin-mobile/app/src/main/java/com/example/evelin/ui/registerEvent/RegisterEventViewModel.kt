package com.example.evelin.ui.registerEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.LoginUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterEventViewModel (private val repository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<LoginUser?>(null)
    val user: StateFlow<LoginUser?> = _user.asStateFlow()


    private val _registrationStatus = MutableStateFlow<String?>(null)
    val registrationStatus: StateFlow<String?> = _registrationStatus.asStateFlow()

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

//    fun registerEvent( eventId: String) {
//        repository.registerEvent( eventId)
//    }

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
    fun submitEventRegistration(eventId: String) {
        viewModelScope.launch {
            try {
                val result = repository.submitEventRegistration(eventId = eventId)
                _registrationStatus.value = result.message
                _navigateToHome.value = true
            } catch (e: Exception) {
                _registrationStatus.value = e.message
                _navigateToHome.value = false
                // Log error or handle appropriately
            }
        }
    }
}