package com.example.evelin.ui.eventDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _events = MutableStateFlow<DataItem?>(null)
    val events: StateFlow<DataItem?> = _events.asStateFlow()

    fun getEvent(id: String) {
        viewModelScope.launch {
            try {
                Log.d("EventDetailViewModel", "Fetching event with id: $id")
                val response = withContext(Dispatchers.IO) { repository.getEvent(id) }
                val event = response.data.event.copy(isRegistered = response.data.isRegistered)
                Log.d("EventDetailViewModel", "Event fetched: $event")
                _events.value = event
            } catch (e: Exception) {
                Log.e("EventDetailViewModel", "Error fetching event: ${e.message}", e)
                _events.value = null // Set null if an error occurs
            }
        }
    }

    fun refreshEvent(id: String) {
        getEvent(id)
    }
}