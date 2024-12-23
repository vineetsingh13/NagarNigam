package com.example.nagarnigam.HomePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nagarnigam.Service
import com.example.nagarnigam.complaints.ComplaintActivity
import com.example.nagarnigam.databinding.ActivityMainBinding
import com.example.nagarnigam.signup.signUpApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customButtonCard.setOnClickListener {
            val intent = Intent(this, ComplaintActivity::class.java)
            startActivity(intent)

            getResponse()
        }
    }

    private fun getResponse(){
        val request= Service.buildService(homeApis::class.java)
        val call=request.getComplaints()

        call.enqueue(object : Callback<List<complaintListRes>> {
            override fun onResponse(
                call: Call<List<complaintListRes>>,
                response: Response<List<complaintListRes>>
            ) {
                if(response.isSuccessful){
                    Log.d("home response",response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<complaintListRes>>, t: Throwable) {
                Log.d("home failure",t.toString())
            }

        })
    }
}