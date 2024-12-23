package com.example.nagarnigam.signup

import com.google.gson.annotations.SerializedName

data class signupReq(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String
)

data class signupRes(
    @SerializedName("message") val message: String
)
