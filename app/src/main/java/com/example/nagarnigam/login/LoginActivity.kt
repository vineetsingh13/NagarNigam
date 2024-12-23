package com.example.nagarnigam.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nagarnigam.HomePage.MainActivity
import com.example.nagarnigam.Service
import com.example.nagarnigam.signup.Signup
import com.example.nagarnigam.databinding.ActivityLoginBinding
import com.example.nagarnigam.otpSection.otpAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.LoginButton.setOnClickListener {
            getResponse()
//            val intent=Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
        }

        binding.createProfile.setOnClickListener {
            val intent = Intent(this@LoginActivity, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun getResponse(){
        val mobile=binding.EmailInputText.text.toString()

        val body= loginReq(mobile)

        val request= Service.buildService(loginInterface::class.java)
        val call=request.login(body)

        call.enqueue(object : Callback<loginRes> {
            override fun onResponse(call: Call<loginRes>, response: Response<loginRes>) {
                if(response.isSuccessful){
                    val res=response.body()
                    Log.d("response",res.toString())
                    val intent=Intent(this@LoginActivity, otpAuth::class.java)
                    intent.putExtra("screenType", "login")
                    intent.putExtra("phone-login", mobile)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<loginRes>, t: Throwable) {
                Log.d("failure",t.toString())
            }

        })
    }
}