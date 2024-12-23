package com.example.nagarnigam.otpSection

import com.google.gson.annotations.SerializedName

data class otpReq(
    @SerializedName("phone") val phone: String,
    @SerializedName("otp") val otp: String
)

data class otpRes(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)

data class User(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String
)
