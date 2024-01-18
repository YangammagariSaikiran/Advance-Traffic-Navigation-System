package com.example.advancetrafficnavigationsystem

import android.app.Activity
import android.util.Log
import android.widget.Toast

fun Activity.showToast(message:Any?){
    Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
}
fun logdata(message: Any?){
    Log.d("TestNavigation","$message")
}