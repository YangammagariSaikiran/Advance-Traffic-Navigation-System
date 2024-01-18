package com.example.advancetrafficnavigationsystem

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminvolunteer : AppCompatActivity() {
     lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminvolunteer)
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)




        readvolunteer()




        findViewById<FloatingActionButton>(R.id.btnaddvolunteer).setOnClickListener {
            val dd=BottomSheetDialog(this)
            dd.setContentView(R.layout.cardaddvolunteer)
            val  etname=dd.findViewById<EditText>(R.id.etname)!!
            val etunum=dd.findViewById<EditText>(R.id.etunum)!!
            val etemail=dd.findViewById<EditText>(R.id.etemail)!!
            val etuaddress=dd.findViewById<EditText>(R.id.etuaddress)!!
            val etucity=dd.findViewById<EditText>(R.id.etucity)!!
            val etupass=dd.findViewById<EditText>(R.id.etupass)!!
            val btnsubmit=dd.findViewById<Button>(R.id.btnsubmit)!!

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
                            RetrofitClient.instance.register(name,num,email,city,pass,add,"Volunteer","Available","register")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@Adminvolunteer,"Successfully Added "+ response.body()!!.message, Toast.LENGTH_SHORT).show()
                                        etname.text!!.clear()
                                        etunum.text!!.clear()
                                        etemail.text!!.clear()
                                        etuaddress.text!!.clear()
                                        etucity.text!!.clear()
                                        etupass.text!!.clear()
                                        readvolunteer()
                                        dd.dismiss()

                                    }
                                })
                        }
                    } else {
                        message(it, "Enter your Number")
                        etunum.setError("Enter your Number")
                    }

                }
            }
            dd.show()
        }
    }

    private fun readvolunteer() {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getvolunteer()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {


                        recyclerView.adapter= Adminviewuser.adminuseradapter(
                            this@Adminvolunteer,
                            response.body()!!.user
                        )


                        Toast.makeText(this@Adminvolunteer, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Adminvolunteer, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    private fun message(it: View, message: String) {
        Snackbar.make(it, "" + message, Snackbar.LENGTH_SHORT).show()
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()

    }
}