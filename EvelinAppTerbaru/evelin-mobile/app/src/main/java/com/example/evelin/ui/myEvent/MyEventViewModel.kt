package com.example.evelin.ui.myEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.DataItem
import com.example.evelin.data.response.EventParticipantsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyEventViewModel (private val repository: UserRepository) : ViewModel() {
    private val _events = MutableStateFlow<List<DataItem>>(emptyList())
    val events: StateFlow<List<DataItem>> = _events.asStateFlow()

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = repository.getMyEvent()
                _events.value = response.data?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace() // Handle error here
            }
        }
    }
}