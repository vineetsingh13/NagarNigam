package com.example.nagarnigam.complaints

import com.google.gson.annotations.SerializedName

data class complaintReq(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String,
    @SerializedName("geolocation") val geolocation: Geolocation,
    @SerializedName("images") val images: List<String>,
)

data class Geolocation(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class complaintRes(
    @SerializedName("message") val message: String
)
