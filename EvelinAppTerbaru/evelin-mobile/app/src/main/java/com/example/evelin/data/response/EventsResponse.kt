package com.example.evelin.data.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
	@SerializedName("error") val error: Boolean,
	@SerializedName("message") val message: String,
	@SerializedName("data") val data: EventData
)


data class EventsResponse(
	@SerializedName("data") val data: List<DataItem?>? = null,
	@SerializedName("error") val error: Boolean? = null,
	@SerializedName("message") val message: String? = null
)

data class EventData(
	@SerializedName("event") val event: DataItem,
	@SerializedName("isRegistered") val isRegistered: Int
)

data class DataItem(
	@SerializedName("createdAt") val createdAt: String? = null,
	@SerializedName("posterUrl") val posterUrl: String? = null,
	@SerializedName("university") val university: String? = null,
	@SerializedName("speaker") val speaker: String? = null,
	@SerializedName("description") val description: String? = null,
	@SerializedName("location") val location: String? = null,
	@SerializedName("id") val id: Int? = null,
	@SerializedName("title") val title: String? = null,
	@SerializedName("category") val category: String? = null,
	@SerializedName("userId") val userId: Int? = null,
	@SerializedName("eventDate") val eventDate: String? = null,
	@SerializedName("updatedAt") val updatedAt: String? = null,
	@SerializedName("isRegistered") val isRegistered: Int? = null
)