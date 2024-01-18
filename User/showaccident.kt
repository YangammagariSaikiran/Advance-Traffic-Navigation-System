package com.example.advancetrafficnavigationsystem.User

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.advancetrafficnavigationsystem.R
import com.example.advancetrafficnavigationsystem.RetrofitClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.advancetrafficnavigationsystem.databinding.ActivityShowaccidentBinding
import com.example.advancetrafficnavigationsystem.logdata
import com.example.advancetrafficnavigationsystem.model.ForDistanceRepsonse
import com.example.crimereportingapp.model.ComplaintsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class showaccident : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowaccidentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowaccidentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getcomplaints()
                .enqueue(object : Callback<ComplaintsResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<ComplaintsResponse>, response: Response<
                            ComplaintsResponse>
                    ) {
                        val k = response.body()?.user
                        for (item in k!!){
                            val geo = Geocoder(this@showaccident).getFromLocationName(item.uaddress, 1)
                            val lat = geo[0].latitude
                            val long = geo[0].longitude
                            mMap = googleMap

                            val sydney = LatLng(lat, long)
                            mMap.addMarker(MarkerOptions().position(sydney).title("${item.Date},${item.uaddress}"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                        }


                    }

                    override fun onFailure(call: Call<ComplaintsResponse>, t: Throwable) {
                        Toast.makeText(this@showaccident, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }
}