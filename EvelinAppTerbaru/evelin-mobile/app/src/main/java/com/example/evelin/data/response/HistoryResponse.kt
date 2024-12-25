package com.example.evelin.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<EventParticipantsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataHistory(

	@field:SerializedName("eventParticipants")
	val eventParticipants: List<EventParticipantsItem>
)

data class EventParticipantsItem(

	@field:SerializedName("eventId")
	val eventId: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("Event")
	val event: Event,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Event(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("posterUrl")
	val posterUrl: String,

	@field:SerializedName("university")
	val university: String,

	@field:SerializedName("speaker")
	val speaker: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("eventDate")
	val eventDate: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
