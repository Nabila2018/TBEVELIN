package com.example.evelin.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("data")
	val dataUser: DataUser,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LoginUser(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("noHp")
	val noHp: String,
	@field:SerializedName("institusi")
	val institusi: String
)

data class DataUser(
	@field:SerializedName("user")
	val user: LoginUser,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)

data class LogoutResponse(
	@SerializedName("success") val success: Boolean,
	@SerializedName("message") val message: String
)