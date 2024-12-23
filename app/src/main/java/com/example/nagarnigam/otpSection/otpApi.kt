package com.example.nagarnigam.otpSection

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface otpApi {

    @POST("verify-otp-new-user")
    fun verifyNewUser(
        @Body body: otpReq
    ): Call<otpRes>

    @POST("verify-otp")
    fun verifyExistingUser(
        @Body body: otpReq
    ): Call<otpRes>
}