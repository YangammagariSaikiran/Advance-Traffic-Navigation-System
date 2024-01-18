package com.example.advancetrafficnavigationsystem.model

data class ForDistanceRepsonse (
    var error:Boolean,
    var message:String,
    var user:ArrayList<UserDistance>
)