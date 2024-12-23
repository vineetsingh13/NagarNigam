package com.example.nagarnigam.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface signUpApi {

    @POST("register")
    fun register(
        @Body body: signupReq
    ): Call<signupRes>
}