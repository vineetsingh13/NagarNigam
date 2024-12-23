package com.example.nagarnigam.HomePage

import retrofit2.Call
import retrofit2.http.GET

interface homeApis {

    @GET("all-complaints")
    fun getComplaints(): Call<List<complaintListRes>>
}