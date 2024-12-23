package com.example.nagarnigam.login

import com.google.gson.annotations.SerializedName

data class loginReq(
    @SerializedName("phone") val phone: String
)

data class loginRes(
    @SerializedName("message") val message: String
)
