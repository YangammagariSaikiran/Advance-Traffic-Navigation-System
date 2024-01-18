package com.example.crimereportingapp.model

import com.ymts0579.model.model.User

class ComplaintsResponse(val error: Boolean, val message:String, var user:ArrayList<Complaints>) {
}