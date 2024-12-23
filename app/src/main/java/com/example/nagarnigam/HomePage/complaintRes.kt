package com.example.nagarnigam.HomePage

import com.google.gson.annotations.SerializedName

data class complaintListRes(
    @SerializedName("_id") val id: String,
    @SerializedName("geolocation") val geolocation: Geolocation,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("timestamp") val timestamp: String,  // You can parse this into a Date object if needed
    @SerializedName("__v") val version: Int
)

data class Geolocation(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
