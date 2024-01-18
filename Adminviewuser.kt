package com.example.advancetrafficnavigationsystem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminviewuser : AppCompatActivity() {
    lateinit var listviewuser: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminviewuser)

        listviewuser=findViewById(R.id.listviewuser)
        listviewuser.layoutManager = LinearLayoutManager(this)
        listviewuser.setHasFixedSize(true)


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getuser()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {


                        listviewuser.adapter= adminuseradapter(this@Adminviewuser,response.body()!!.user)


                        Toast.makeText(this@Adminviewuser, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Adminviewuser, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }



    class adminuseradapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<adminuseradapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val tvfname: TextView =view.findViewById(R.id.tvfname);
            val tvfemail: TextView =view.findViewById(R.id.tvfemail);
            val tvfnum: TextView =view.findViewById(R.id.tvfnum);
            val tvfcity: TextView =view.findViewById(R.id.tvfcity);

            val tvfadder:TextView=view.findViewById(R.id.tvfadder)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.carduseradmin, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            val kk=listdata.get(position)
            holder.tvfname.text=kk.name
            holder.tvfemail.text=kk.email
            holder.tvfnum.text=kk.moblie
            holder.tvfcity.text=kk.city

            holder.tvfadder.text=kk.address







        }




        override fun getItemCount() = listdata.size
    }
}