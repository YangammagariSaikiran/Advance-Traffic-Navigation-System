package com.example.advancetrafficnavigationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)


        findViewById<Button>(R.id.btnadminvolunteer).setOnClickListener {
            startActivity(Intent(this,Adminvolunteer::class.java))
        }
        findViewById<Button>(R.id.btnadminuser).setOnClickListener {
            startActivity(Intent(this,Adminviewuser::class.java))
        }
        findViewById<Button>(R.id.btnadminlogout).setOnClickListener {
            val alertdialog= AlertDialog.Builder(this)
            alertdialog.setIcon(R.drawable.ic_launcher_foreground)
            alertdialog.setTitle("LOGOUT")
            alertdialog.setIcon(R.drawable.logo)
            alertdialog.setCancelable(false)
            alertdialog.setMessage("Do you Want to Logout?")
            alertdialog.setPositiveButton("Yes"){ alertdialog, which->
                startActivity(Intent(this, Login::class.java))
                finish()
                val  shared=getSharedPreferences("user", MODE_PRIVATE)
                shared.edit().clear().apply()
                alertdialog.dismiss()
            }
            alertdialog.setNegativeButton("No"){alertdialog,which->
                Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
                alertdialog.dismiss()
            }
            alertdialog.show()
        }
    }
}