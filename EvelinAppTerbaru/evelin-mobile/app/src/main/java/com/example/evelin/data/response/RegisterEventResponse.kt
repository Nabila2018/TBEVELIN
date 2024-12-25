package com.example.evelin.data.response

import com.google.gson.annotations.SerializedName

data class RegisterEventResponse(

	@field:SerializedName("data")
	val data: DataRegEvent? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)



data class DataRegEvent(

	@field:SerializedName("eventId")
	val eventId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,


)
