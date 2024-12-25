package com.example.evelin.ui.addEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evelin.data.UserRepository
import com.example.evelin.data.response.AddEventResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddEventViewModel(private val repository: UserRepository) : ViewModel() {

    private val _addEventResult = MutableLiveData<Boolean>()
    val addEventResult: LiveData<Boolean> get() = _addEventResult

    fun addEvent(
        title: RequestBody,
        description: RequestBody,
        eventDate: RequestBody,
        location: RequestBody,
        category: RequestBody,
        posterUrl: MultipartBody.Part,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {

                repository.addEvent(title, description, eventDate, location, category, posterUrl)
                _addEventResult.postValue(true)
                onSuccess()
            } catch (e: Exception) {
                _addEventResult.postValue(false)
                onError(e)
            }
        }
    }
}