package com.example.advancetrafficnavigationsystem.User

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.advancetrafficnavigationsystem.R
import com.example.advancetrafficnavigationsystem.RetrofitClient
import com.example.crimereportingapp.model.Complaints
import com.example.crimereportingapp.model.ComplaintsResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History : AppCompatActivity() {
    lateinit var listhistory: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        listhistory=findViewById(R.id.listhistory)
        listhistory.layoutManager = LinearLayoutManager(this)
        listhistory.setHasFixedSize(true)


        val email=getSharedPreferences("user", MODE_PRIVATE).getString("email", "")!!


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewusercomplaints(email,"viewusercomplaints")
                .enqueue(object : Callback<ComplaintsResponse> {

                    override fun onResponse(call: Call<ComplaintsResponse>, response: Response<ComplaintsResponse>) {

                        listhistory.adapter=historyadapter(this@History,response.body()!!.user)
                        Toast.makeText(this@History, "${response.body()!!.user}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ComplaintsResponse>, t: Throwable) {
                        Toast.makeText(this@History, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }


    class historyadapter(var context: Context, var listdata: ArrayList<Complaints>):
        RecyclerView.Adapter<historyadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            var tvdes=view.findViewById<TextView>(R.id.tvdes)
            var tvdate=view.findViewById<TextView>(R.id.tvdate)
            var tvstatus=view.findViewById<TextView>(R.id.tvstatus)
            var tvcity=view.findViewById<TextView>(R.id.tvcity)
            var viewimage=view.findViewById<ImageView>(R.id.viewimage)
            var tvaddress=view.findViewById<TextView>(R.id.tvaddress)
            var tvworkers=view.findViewById<TextView>(R.id.tvworkers)
            var linearworker=view.findViewById<LinearLayout>(R.id.linearworker)
            var tvuname1=view.findViewById<TextView>(R.id.tvuname1)
            var tvuemail1=view.findViewById<TextView>(R.id.tvuemail1)
            var tvunum1=view.findViewById<TextView>(R.id.tvunum1)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardhistory, parent, false)
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

                    tvuname1.text="Name:- "+this.vname
                    tvuemail1.text="Email:- "+this.vemail
                    tvunum1.text="Number:- "+this.vnum
                    Glide.with(context).load(Uri.parse(path.trim())).into(viewimage)

                    linearworker.visibility= View.GONE


                    tvworkers.setOnClickListener {
                        linearworker.visibility= View.VISIBLE
                        Handler().postDelayed({
                            linearworker.visibility= View.GONE
                        },5000)
                    }


                    itemView.setOnClickListener {
                        if(this.feedback.isNotEmpty()){
                            Toast.makeText(context, "Already given your Feedback", Toast.LENGTH_SHORT).show()
                        }else{
                            val dd=BottomSheetDialog(context)
                            dd.setContentView(R.layout.cardfeedback)
                            val etfeedback=dd.findViewById<EditText>(R.id.etfeedback)!!
                            dd.findViewById<Button>(R.id.btnfeedback)!!.setOnClickListener {
                                val feedback=etfeedback.text.toString().trim()
                                if(feedback.isEmpty()){
                                    Toast.makeText(context, "Enter your Feedback", Toast.LENGTH_SHORT).show()
                                    etfeedback.setError("Enter your Feedback")
                                }else{
                                    readfeeback(feedback,id)
                                    dd.dismiss()
                                }
                            }
                            dd.show()
                        }



                    }

                }
            }

        }

        private fun readfeeback(feedback: String, id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatefeedback(id,feedback,"updatefeedback")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()


                        }
                    })
            }
        }


        override fun getItemCount() = listdata.size
    }
}