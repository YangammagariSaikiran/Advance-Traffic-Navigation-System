package com.example.advancetrafficnavigationsystem.User

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.advancetrafficnavigationsystem.R


class Userhome : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_userhome, container, false)
        requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            view.findViewById<TextView>(R.id.tvname).setText("welcome "+getString("name", "").toString())
        }

        view.findViewById<Button>(R.id.btnaddrequest).setOnClickListener {
            startActivity(Intent(requireActivity(),Addrequest::class.java))
        }
        view.findViewById<Button>(R.id.btnhistory).setOnClickListener {
            startActivity(Intent(requireActivity(),History::class.java))
        }

        view.findViewById<Button>(R.id.viewaccidentzone).setOnClickListener {
            startActivity(Intent(requireActivity(),showaccident::class.java))
        }
        return view
    }

}