package com.example.nagarnigam.complaints

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface complaintApi {

    @POST("complaints")
    fun submitComplaint(
        @Header("Content-Type") contentType: String="application/json",
        @Header("Authorization") authorization: String,
        @Body body: complaintReq,

    ): Call<complaintRes>
}