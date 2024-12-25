package com.example.evelin.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.DataHistory
import com.example.evelin.data.response.DataItem
import com.example.evelin.data.response.EventParticipantsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventHistoryViewModel (private val repository: UserRepository) : ViewModel() {
    private val _events = MutableStateFlow<List<EventParticipantsItem>>(emptyList())
    val events: StateFlow<List<EventParticipantsItem>> = _events.asStateFlow()

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = repository.getHistory()
                _events.value = response.data
            } catch (e: Exception) {
                e.printStackTrace() // Handle error here
            }
        }
    }
}