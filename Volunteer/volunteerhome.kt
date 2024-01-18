package com.example.advancetrafficnavigationsystem.Volunteer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.advancetrafficnavigationsystem.R
import com.example.advancetrafficnavigationsystem.RetrofitClient
import com.example.crimereportingapp.model.Complaints
import com.example.crimereportingapp.model.ComplaintsResponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class volunteerhome : Fragment() {
    lateinit var tvname: TextView
    lateinit var tvstatus: TextView
    lateinit var btnavaialble: Button
    lateinit var btnnotavaiable: Button
    lateinit var listworkercomplaint: RecyclerView
    var name=""
    var num=""
    var email=""
    var address=""
    var city=""
    var pass=""
    var type=""
    var stat=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_volunteerhome, container, false)
        tvname=view.findViewById(R.id.tvname)
        tvstatus=view.findViewById(R.id.tvstatus)
        btnavaialble=view.findViewById(R.id.btnavaialble)
        btnnotavaiable=view.findViewById(R.id.btnnotavaiable)
        listworkercomplaint=view.findViewById(R.id.listworkercomplaint)
        listworkercomplaint.layoutManager = LinearLayoutManager(requireActivity())
        listworkercomplaint.setHasFixedSize(true)


        var id=0
        requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id=getInt("id",0)
            tvname.text = "Welcome " + getString("name", "").toString()
            tvstatus.text = "status ${getString("status", "").toString()}"
            name=getString("name","").toString()
            num=getString("num","").toString()
            email=getString("email","").toString()
            address=getString("address","").toString()
            city=getString("city","").toString()
            pass=getString("pass","").toString()
            type=getString("type","").toString()
            stat=getString("status1","").toString()
        }

        btnavaialble.setOnClickListener {  needtoupdate("Available",id)}
        btnnotavaiable.setOnClickListener { needtoupdate("Not Available",id)  }


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewworkcomplaints(email,"viewworkcomplaints")
                .enqueue(object : Callback<ComplaintsResponse> {

                    override fun onResponse(call: Call<ComplaintsResponse>, response: Response<ComplaintsResponse>) {

                        listworkercomplaint.adapter=workcomplaintadapter(requireActivity(),response.body()!!.user)
                        Toast.makeText(requireActivity(), "${response.body()!!.user}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ComplaintsResponse>, t: Throwable) {
                        Toast.makeText(requireActivity(), "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
        return view
    }

    private fun needtoupdate(status: String, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.updatestatus(status,id,"updatestatus")

                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        t.message?.let { it1 ->
                            Toast.makeText(requireActivity(),t.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        Toast.makeText(requireActivity(), response.body()!!.message, Toast.LENGTH_SHORT).show()

                        if(response.body()!!.message=="success"){
                            tvstatus.setText("status $status")
                            requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                                putString("num",num)
                                putString("pass",pass)
                                putString("email",email)
                                putString("name",name)
                                putString("address",address)
                                putString("type",type)
                                putString("status",status)
                                putString("status1",stat)
                                putString("city",city)
                                putInt("id",id)
                                apply()
                            }
                        }
                    }
                })
        }
    }


    class workcomplaintadapter  (var context: Context, var listdata: ArrayList<Complaints>):
        RecyclerView.Adapter<workcomplaintadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            var tvdes=view.findViewById<TextView>(R.id.tvdes)
            var tvdate=view.findViewById<TextView>(R.id.tvdate)
            var tvstatus=view.findViewById<TextView>(R.id.tvstatus)
            var tvcity=view.findViewById<TextView>(R.id.tvcity)
            var viewimage=view.findViewById<ImageView>(R.id.viewimage)
            var tvuser=view.findViewById<TextView>(R.id.tvuser)
            var linearuser=view.findViewById<LinearLayout>(R.id.linearuser)
            var tvuname=view.findViewById<TextView>(R.id.tvuname)
            var tvuemail=view.findViewById<TextView>(R.id.tvuemail)
            var tvunum=view.findViewById<TextView>(R.id.tvunum)
            var tvaddress=view.findViewById<TextView>(R.id.tvaddress)
            var imageloc=view.findViewById<ImageView>(R.id.imageloc)
            var tvfeedback=view.findViewById<TextView>(R.id.tvfeedback)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardworkcomplaints, parent, false)
            return DataViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {

            holder.apply {
                listdata.get(position).apply {


                    tvdes.text=this.Description
                    tvdate.text=this.Date
                    tvstatus.text=this.status
                    tvcity.text=this.ucity
                    tvaddress.text=this.uaddress
                    tvfeedback.text=feedback

                    tvuname.text="Name:- "+this.uname
                    tvuemail.text="Email:- "+this.uemail
                    tvunum.text="Number:- "+this.unum

                    Glide.with(context).load(Uri.parse(path.trim())).into(viewimage)
                    linearuser.visibility= View.GONE

                    if(feedback.isNotEmpty()){
                        tvfeedback.visibility=View.VISIBLE
                    }else{
                        tvfeedback.visibility=View.GONE

                    }

                    tvuser.setOnClickListener {
                        linearuser.visibility= View.VISIBLE
                        Handler().postDelayed({
                            linearuser.visibility= View.GONE
                        },5000)
                    }

                    imageloc.setOnClickListener {
                        val iii=Intent(context,MapsActivity::class.java)
                        iii.putExtra("city",this.uaddress)
                        context.startActivity(iii)

                    }



                    itemView.setOnClickListener {
                        if(this.status=="Completed"){
                            Toast.makeText(context, "You Already Solved the Complaint", Toast.LENGTH_SHORT).show()
                        }else{
                            val alertdialog= AlertDialog.Builder(context)
                            alertdialog.setTitle("Are You Sure Complaint is Solved?")
                            alertdialog.setIcon(R.drawable.logo)
                            alertdialog.setCancelable(false)

                            alertdialog.setPositiveButton("yes"){ alertdialog, which->
                                CoroutineScope(Dispatchers.IO).launch {
                                    RetrofitClient.instance.updatestatuscomplaints("Completed",id,"updatestatuscomplaints")
                                        .enqueue(object: Callback<DefaultResponse> {
                                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                                            }
                                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                                                if (TextUtils.isDigitsOnly(unum)) {
                                                    val smsManager: SmsManager = SmsManager.getDefault()
                                                    smsManager.sendTextMessage(unum, null, "Your Complaint is resolved", null, null)
                                                    Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                                    context.startActivity(Intent(context,Volunteerdashboard::class.java))

                                                }
                                            }
                                        })
                                }

                                alertdialog.dismiss()
                            }

                            alertdialog.show()
                        }
                    }



                }
            }

        }


        override fun getItemCount() = listdata.size
    }

}