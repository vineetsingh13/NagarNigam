package com.example.nagarnigam.login

import com.example.nagarnigam.signup.signupReq
import com.example.nagarnigam.signup.signupRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface loginInterface {

    @POST("login")
    fun login(
        @Body body: loginReq
    ): Call<loginRes>
}