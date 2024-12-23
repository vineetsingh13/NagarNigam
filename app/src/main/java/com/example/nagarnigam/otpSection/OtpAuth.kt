package com.example.nagarnigam.otpSection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nagarnigam.HomePage.MainActivity
import com.example.nagarnigam.Service
import com.example.nagarnigam.databinding.ActivityOtpAuthBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otpAuth : AppCompatActivity() {

    var phone: String? = null

    private lateinit var binding: ActivityOtpAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.LoginButton.setOnClickListener {
            val screenType = intent.getStringExtra("screenType")

            if (screenType == "login") {
                phone=intent.getStringExtra("phone-login")
                callLoginAPI()
            } else if (screenType == "signup") {
                phone=intent.getStringExtra("phone-signup")
                callSignupAPI()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun callSignupAPI() {
        val otp = binding.otpBox1.text.toString() +
                binding.otpBox2.text.toString() +
                binding.otpBox3.text.toString() +
                binding.otpBox4.text.toString() +
                binding.otpBox5.text.toString() +
                binding.otpBox6.text.toString()
        val body = otpReq(phone!!,otp)

        val request= Service.buildService(otpApi::class.java)
        val call=request.verifyNewUser(body)
        call.enqueue(object : Callback<otpRes> {
            override fun onResponse(call: Call<otpRes>, response: Response<otpRes>) {
                Log.d("otp",response.toString())
                if(response.isSuccessful){
                    val intent = Intent(this@otpAuth, MainActivity::class.java)
                    intent.putExtra("token", response.body()?.token)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<otpRes>, t: Throwable) {
                Log.d("otp-error",t.toString())
            }

        })
    }

    private fun callLoginAPI() {
        val otp = binding.otpBox1.text.toString() +
                binding.otpBox2.text.toString() +
                binding.otpBox3.text.toString() +
                binding.otpBox4.text.toString() +
                binding.otpBox5.text.toString() +
                binding.otpBox6.text.toString()
        val body = otpReq(phone!!,otp)

        val request= Service.buildService(otpApi::class.java)
        val call=request.verifyExistingUser(body)
        call.enqueue(object : Callback<otpRes> {
            override fun onResponse(call: Call<otpRes>, response: Response<otpRes>) {
                Log.d("otp",response.toString())
                if(response.isSuccessful){
                    val intent = Intent(this@otpAuth, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<otpRes>, t: Throwable) {
                Log.d("otp-error",t.toString())
            }

        })
    }
}