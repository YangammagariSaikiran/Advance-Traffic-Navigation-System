package com.example.advancetrafficnavigationsystem



import com.example.advancetrafficnavigationsystem.model.ForDistanceRepsonse
import com.example.crimereportingapp.model.ComplaintsResponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name") name:String,
        @Field("mobile")mobile:String,
        @Field("email")email :String,
        @Field("city") city:String,
        @Field("password") password:String,
        @Field("address")address :String,
        @Field("type") type:String,
        @Field("status") status:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("password") password:String,
              @Field("condition") condition:String): Call<LoginResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun adminusercity( @Field("city") city:String,
              @Field("condition") condition:String): Call<ForDistanceRepsonse>


    @GET("Getvolunteers.php")
    fun getvolunteer():Call<Userresponse>


    @GET("Getuser.php")
    fun getuser():Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name:String, @Field("mobile")moblie:String, @Field("password") password:String,
        @Field("address")address :String, @Field("city") city:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun updatestatus(
        @Field("status") status:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Complaints.php")
    fun addcomplaint(
        @Field("uname") uname:String,
        @Field("unum") unum:String,
        @Field("uemail") uemail:String,
        @Field("uaddress") uaddress:String,
        @Field("ucity") ucity:String,
        @Field("status") status:String,
        @Field("Description") Description:String,
        @Field("Date") Date:String,
        @Field("path")path:String,
        @Field("vemail")vemail:String,
        @Field("vname")vname:String,
        @Field("vnum")vnum:String,
        @Field("feedback")feedback:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Complaints.php")
    fun  viewusercomplaints(
        @Field("uemail")uemail:String,
        @Field("condition") condition:String,
    ): Call<ComplaintsResponse>

    @FormUrlEncoded
    @POST("Complaints.php")
    fun  viewworkcomplaints(
        @Field("vemail") vemail:String,
        @Field("condition") condition:String,
    ): Call<ComplaintsResponse>


    @FormUrlEncoded
    @POST("Complaints.php")
    fun  updatestatuscomplaints(
        @Field("status") status:String,
        @Field("id") id: Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @GET("getcomplaints.php")
    fun getcomplaints():Call<ComplaintsResponse>


    @FormUrlEncoded
    @POST("Complaints.php")
    fun updatefeedback(
        @Field("id") id: Int,
        @Field("feedback")feedback:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

}