package com.example.advancetrafficnavigationsystem.User

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.advancetrafficnavigationsystem.R
import com.example.advancetrafficnavigationsystem.RetrofitClient
import com.example.advancetrafficnavigationsystem.logdata
import com.example.advancetrafficnavigationsystem.model.ForDistanceRepsonse
import com.example.advancetrafficnavigationsystem.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class Addrequest : AppCompatActivity() {
    lateinit var btncamera: Button
    lateinit var btnref: Button
    lateinit var btnupload: Button
    lateinit var desc: EditText
    lateinit var ivphoto: ImageView
    private lateinit var fused: FusedLocationProviderClient
    var place=""
    var name=""
    var num1=""
    var email=""
    var city=""
    var vemail=""
    var vnum=""
    var vname=""
var enoded=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addrequest)
        btncamera=findViewById(R.id.btncamera)
        btnref=findViewById(R.id.btnref)
        btnupload=findViewById(R.id.btnupload)
        desc=findViewById(R.id.desc)
        ivphoto=findViewById(R.id.ivphoto)
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            name=getString("name","").toString()
            num1=getString("mob","").toString()
            email=getString("email","").toString()

        }


        fused= LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),10)
            }
        }else{
            fused.lastLocation.addOnSuccessListener{
                place= Geocoder(this).getFromLocation(it.latitude,it.longitude,1)[0].getAddressLine(0)
                city= Geocoder(this).getFromLocation(it.latitude,it.longitude,1)[0].locality
               // Toast.makeText(this, "$place", Toast.LENGTH_SHORT).show()
            }
            fused.lastLocation.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
            fused.lastLocation.addOnCanceledListener {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        val activity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it!=null){
                val uri= Uri.parse(it.data!!.data.toString())
                val data= MediaStore.Images.Media.getBitmap(contentResolver,uri)
                val out= ByteArrayOutputStream()
                ivphoto.setImageBitmap(data)
                data.compress(Bitmap.CompressFormat.PNG,100,out)

                enoded = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
            }
        }
        btncamera.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type="image/*"
                activity.launch(this)
            }
        }
        btnref.setOnClickListener {
            ivphoto.setImageResource(R.drawable.add_a_photo)
            desc.setText("")
        }



        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.adminusercity(city,"adminusercity")
                .enqueue(object : Callback<ForDistanceRepsonse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<ForDistanceRepsonse>, response: Response<
                            ForDistanceRepsonse>) {
                        val k = response.body()?.user

                        k?.forEach {
                            val geo = Geocoder(this@Addrequest).getFromLocationName(place, 1)
                            val lat = geo[0].latitude
                            val long = geo[0].longitude
                            val ge = Geocoder(this@Addrequest).getFromLocationName(it.address, 1)
                            val lat1 = ge[0].latitude
                            val long1 = ge[0].longitude
                            val locationA = Location("point A")
                            locationA.latitude = lat
                            locationA.longitude = long
                            val locationB = Location("point B")
                            locationB.latitude = lat1
                            locationB.longitude = long1
                            it.distance = (locationA.distanceTo(locationB) / 1000).toInt()
                        }
                        k?.sortBy { it.distance }
                       k?.getOrNull(0)?.apply {
                           vemail=email.toString()
                           vnum=moblie.toString()
                           vname=name.toString()
                           Toast.makeText(this@Addrequest, "${vemail},${vnum},${vname}", Toast.LENGTH_SHORT).show()
                         }
                        logdata(k)
                    }

                    override fun onFailure(call: Call<ForDistanceRepsonse>, t: Throwable) {
                        Toast.makeText(this@Addrequest, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }



        btnupload.setOnClickListener {
            val address=place
            val des=desc.text.toString().trim()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val Date: String = sdf.format(Date())


            if(des.isEmpty()){
                Toast.makeText(this, "Enter your Description", Toast.LENGTH_SHORT).show()
            }else if(enoded==""){
                Toast.makeText(this, "Upload the pic", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.addcomplaint(name,num1,email,address,city,"Pending",des,Date,enoded,vemail,vname,vnum,"","addComplaints")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(this@Addrequest, ""+t.message, Toast.LENGTH_SHORT).show()
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                Toast.makeText(this@Addrequest, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                ivphoto.setImageResource(R.drawable.add_a_photo)
                                desc.setText("")
                                startActivity(Intent(this@Addrequest,History::class.java))
                            }
                        })
                }
            }


        }
    }



}