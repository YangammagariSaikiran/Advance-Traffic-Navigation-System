package com.example.advancetrafficnavigationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.advancetrafficnavigationsystem.User.Userdashboard
import com.example.advancetrafficnavigationsystem.Volunteer.Volunteerdashboard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val type=getSharedPreferences("user", MODE_PRIVATE).getString("type", "")!!

        if(type=="admin"){
            startActivity(Intent(this,AdminDashboard::class.java))
            finish()
        }else if(type=="Volunteer"){
            startActivity(Intent(this,Volunteerdashboard::class.java))
            finish()
        }else if(type=="User"){
             startActivity(Intent(this, Userdashboard::class.java))
            finish()
        }else{
            findViewById<Button>(R.id.btnstart).setOnClickListener {
                startActivity(Intent(this,Login::class.java))
                finish()
            }
        }



    }
}