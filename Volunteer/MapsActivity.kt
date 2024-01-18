package com.example.advancetrafficnavigationsystem.Volunteer

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.advancetrafficnavigationsystem.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.advancetrafficnavigationsystem.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var to=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        to=intent.getStringExtra("city").toString()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val geocoder = Geocoder(this).getFromLocationName(to, 1).get(0)
        val location = LatLng(geocoder.latitude, geocoder.longitude)
        mMap.addMarker(MarkerOptions().position(location).title(to))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
    }
}