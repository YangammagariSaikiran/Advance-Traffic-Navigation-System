package com.example.advancetrafficnavigationsystem.model

import com.google.gson.annotations.SerializedName

data class UserDistance (
    @SerializedName("id")var id:Int?=null,
    @SerializedName("type")var type:String?=null,
    @SerializedName("name")var name:String?=null,
    @SerializedName("moblie")var moblie:String?=null,
    @SerializedName("email")var email:String?=null,
    @SerializedName("city")var city:String?=null,
    @SerializedName("password")var password:String?=null,
    @SerializedName("address")var address:String?=null,
    @SerializedName("status")var status:String?=null,
    @SerializedName("distance")var distance:Int?=null
)