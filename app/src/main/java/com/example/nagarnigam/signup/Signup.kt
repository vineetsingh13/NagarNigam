package com.example.nagarnigam.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nagarnigam.Service
import com.example.nagarnigam.databinding.ActivitySignupBinding
import com.example.nagarnigam.otpSection.otpAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginButton.setOnClickListener {

            getResponse()
        }

    }

    private fun getResponse(){
        val name=binding.EmailInputText.text.toString()
        val phone=binding.phoneInputText.text.toString()
        val body=signupReq(name,phone)
        val request= Service.buildService(signUpApi::class.java)
        val call=request.register(body)

        call.enqueue(object : Callback<signupRes> {
            override fun onResponse(call: Call<signupRes>, response: Response<signupRes>) {
                if(response.isSuccessful){
                    val res=response.body()
                    Log.d("response",res.toString())
                    val intent=Intent(this@Signup, otpAuth::class.java)
                    intent.putExtra("screenType", "signup")
                    intent.putExtra("phone-signup", phone)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<signupRes>, t: Throwable) {
                Log.d("failure",t.toString())
            }

        })
    }
}