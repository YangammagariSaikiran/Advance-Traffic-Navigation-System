package com.example.advancetrafficnavigationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.advancetrafficnavigationsystem.User.Userdashboard
import com.example.advancetrafficnavigationsystem.Volunteer.Volunteerdashboard
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    lateinit var linearregister:LinearLayout
    lateinit var linearegister:LinearLayout
    lateinit var linearlogin:LinearLayout
    lateinit var btnlogin:ImageView
    lateinit var etemail1:EditText
    lateinit var etpassword:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        linearregister=findViewById(R.id.linearregister)
        linearegister=findViewById(R.id.linearegister)
        linearlogin=findViewById(R.id.linearlogin)
        btnlogin=findViewById(R.id.btnlogin)
        etemail1=findViewById(R.id.etemail1)
        etpassword=findViewById(R.id.etpassword)


        btnlogin.setOnClickListener {
            linearegister.visibility= View.GONE
            linearlogin.visibility=View.VISIBLE
            val email=etemail1.text.toString().trim()
            val pass=etpassword.text.toString().trim()
            if(email.isEmpty()){
                message(it,"Enter your Email")
                etemail1.setError("Enter your Email")
            }else if(pass.isEmpty()){
                message(it,"Enter your Email")
                etpassword.setError("Enter your Email")
            }else if(email.contains("admin")&& pass.contains("admin")){
                getSharedPreferences("user", MODE_PRIVATE).edit().putString("type","admin").apply()
                startActivity(Intent(this,AdminDashboard::class.java))
                finish()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.login(email,pass,"login")
                        .enqueue(object: Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>, response: Response<LoginResponse>
                            ) {
                                if(!response.body()?.error!!){
                                    val type=response.body()?.user
                                    if (type!=null) {
                                        getSharedPreferences("user", MODE_PRIVATE).edit().apply {
                                            putString("mob",type.moblie)
                                            putString("pass",type.password)
                                            putString("email",type.email)
                                            putString("name",type.name)
                                            putString("address",type.address)
                                            putString("city",type.city)
                                            putString("type",type.type)
                                            putString("status",type.status)
                                            putInt("id",type.id)
                                            apply()
                                        }

                                        val kk=type.type
                                        if(kk=="Volunteer"){
                                            startActivity(Intent(this@Login,Volunteerdashboard::class.java))
                                            finish()
                                        }else if(kk=="User"){
                                            startActivity(Intent(this@Login, Userdashboard::class.java))
                                            finish()
                                        }
                                    }
                                }else{
                                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                            }

                        })
                }
            }
        }


        linearregister.setOnClickListener {
            linearegister.visibility= View.VISIBLE
            linearlogin.visibility=View.GONE

          val  etname=findViewById<EditText>(R.id.etname)
           val etunum=findViewById<EditText>(R.id.etunum)
           val etemail=findViewById<EditText>(R.id.etemail)
           val etuaddress=findViewById<EditText>(R.id.etuaddress)
           val etucity=findViewById<EditText>(R.id.etucity)
           val etupass=findViewById<EditText>(R.id.etupass)
           val btnsubmit=findViewById<Button>(R.id.btnsubmit)

            btnsubmit.setOnClickListener {
                val name=etname.text.toString().trim()
                val num=etunum.text.toString().trim()
                val email=etemail.text.toString().trim()
                val add=etuaddress.text.toString().trim()
                val city=etucity.text.toString().trim()
                val pass=etupass.text.toString().trim()
                if (name.isEmpty()) {
                    message(it, "Enter your Name")
                    etname.error = "Enter your Name"
                } else if (num.isEmpty()) {
                    message(it, "Enter your Number")
                    etunum.setError("Enter your Number")
                } else if (email.isEmpty()) {
                    message(it, "Enter your Email")
                    etemail.setError("Enter your Mail")
                } else if (add.isEmpty()) {
                    message(it, "Enter your Address")
                    etuaddress.setError("Enter your Address")
                } else if (pass.isEmpty()) {
                    message(it, "Enter your password")
                    etupass.setError("Enter your Password")
                } else if (city.isEmpty()) {
                    message(it, "Enter your City")
                    etucity.setError("Enter your City")
                } else if (!email.contains("@gmail.com")) {
                    message(it, "Enter your Email Properly")
                    etemail.setError("Enter your Email Properly")
                }else{
                    if (num.count() == 10) {
                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.register(name,num,email,city,pass,add,"User","","register")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@Login,"Your Registration Successful "+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                                        etname.text!!.clear()
                                        etunum.text!!.clear()
                                        etemail.text!!.clear()
                                        etuaddress.text!!.clear()
                                        etucity.text!!.clear()
                                        etupass.text!!.clear()
                                        etemail1.setText(email)
                                        etpassword.setText(pass)
                                        linearegister.visibility= View.GONE
                                        linearlogin.visibility=View.VISIBLE

                                    }
                                })
                        }
                    } else {
                        message(it, "Enter your Number")
                        etunum.setError("Enter your Number")
                    }

                }
            }

        }
    }

    private fun message(it: View, message: String) {
        Snackbar.make(it, "" + message, Snackbar.LENGTH_SHORT).show()
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()

    }
}