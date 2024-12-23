package com.example.nagarnigam.complaints

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nagarnigam.databinding.ActivityComplaintBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale
import com.cloudinary.*;
import com.example.nagarnigam.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class ComplaintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var hasNotificationPermissionGranted = false
    var lastNumber:String?=null
    var latitude:Double?=null
    var longitude:Double?=null

    private var map: GoogleMap? = null
    private val cloudinary = Cloudinary("cloudinary://197865315677827:wM3xWtlVvQVZmn7GN4dyKMw5-vI@dikkucfuf")

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.selectedImageView.visibility= View.VISIBLE
            binding.selectedImageView.setImageURI(uri)
            lastNumber = extractLastNumberFromUri(uri.toString())

            Log.d("PhotoPicker", uri.toString())
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        notificationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.useCurrentLocation.setOnClickListener {
            getCurrentLocation(binding.selectLocationInput)
            binding.selectLocationButton.visibility = View.GONE
        }

        binding.selectLocationManually.setOnClickListener {
            binding.selectLocationInput.text?.clear()
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync{
            map=it
        }
        setupAddressInputListener()

        binding.submitButton.setOnClickListener{
            uploadComplaint()
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(locationResult: TextView) {

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                val address = getAddressFromLatLng(latitude!!, longitude!!)
                locationResult.text = "Address: $address"
                val location = LatLng(latitude!!, longitude!!)
                map?.addMarker(MarkerOptions().position(location).title("Selected Location"))

                // Move and zoom the camera to the location
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            } else {
                locationResult.text = "Unable to get current location"
            }
        }
    }

    private fun setupAddressInputListener() {
        binding.selectLocationInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 3) {
                    searchLocation(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchLocation(query: String) {

        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                latitude=address.latitude
                longitude=address.longitude
                val latLng = LatLng(address.latitude, address.longitude)

                updateMap(latLng, query)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateMap(latLng: LatLng, title: String) {
        map?.clear()
        map?.addMarker(MarkerOptions().position(latLng).title(title))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${this.packageName}")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                // Format the address as a string
                address.getAddressLine(0) ?: "No address found"
            } else {
                "No address found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unable to get address"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    fun extractLastNumberFromUri(uri: String): String? {
        val regex = """(\d+)$""".toRegex()
        val matchResult = regex.find(uri)
        return matchResult?.groups?.get(1)?.value
    }

    fun uploadComplaint(){
        val title=binding.titleInput.text.toString()
        val description=binding.descriptionInput.text.toString()
        val address=binding.selectLocationInput.text.toString()
        val image_url=cloudinary.image{
            publicId(lastNumber.toString())
            assetType("raw")
        }
        val phone1=intent.getStringExtra("phone-login")
        val phone2=intent.getStringExtra("phone-signup")
        val geolocation = Geolocation(latitude!!, longitude!!)
        val userId = phone1 ?: phone2

        val body = complaintReq(
            title = title,
            description = description,
            location = address,
            geolocation = geolocation,
            images = listOf(image_url.toString()),
        )
        val token=intent.getStringExtra("token")

        val request= Service.buildService(complaintApi::class.java)
        val call=request.submitComplaint(authorization = "Bearer $token", body = body)
        Log.d("token",token.toString())
        Log.d("body",body.toString())
        call.enqueue(object : Callback<complaintRes> {
            override fun onResponse(call: Call<complaintRes>, response: Response<complaintRes>) {
                if(response.isSuccessful){
                    Log.d("submit complaint",response.body().toString())
                }
            }

            override fun onFailure(call: Call<complaintRes>, t: Throwable) {
                Log.d("complaint failure",t.toString())
            }

        })

    }
}