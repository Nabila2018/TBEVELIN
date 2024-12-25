package com.example.evelin.ui.editEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.DataItem
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class EditEventViewModel(private val repository: UserRepository) : ViewModel() {
    // Event details LiveData
    private val _eventDetails = MutableLiveData<DataItem?>()
    val eventDetails: LiveData<DataItem?> = _eventDetails

    // Update result LiveData
    private val _updateEventResult = MutableLiveData<Boolean>()
    val updateEventResult: LiveData<Boolean> = _updateEventResult

    // Fetch event details
    fun fetchEventDetails(eventId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getEvent(eventId)
                _eventDetails.value = response.data.event
            } catch (e: Exception) {
                // Handle error - perhaps set an error state
                _eventDetails.value = null
            }
        }
    }

    // Update event method
    fun updateEvent(
        eventId: String,
        eventDate: String,
        location: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateEvent(
                    eventId = eventId,
                    eventDate = eventDate,
                    location = location
                )
                _updateEventResult.value = true
                onSuccess()
            } catch (e: HttpException) {
                _updateEventResult.value = false
                onError(e)
            } catch (e: Exception) {
                _updateEventResult.value = false
                onError(e)
            }
        }
    }

    // Data class to represent event details
    data class EventDetailsState(
        val id: String,
        val title: String,
        val description: String,
        val eventDate: String,
        val location: String,
        val category: String,
        val posterUrl: String
    )
}