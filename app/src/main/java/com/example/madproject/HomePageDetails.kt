package com.example.madproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class HomePageDetails : AppCompatActivity() {

    private lateinit var tvproID : TextView
    private lateinit var tvproname: TextView
    private lateinit var tvprocat: TextView
    private lateinit var tvinnoname: TextView
    private lateinit var tvadddes: TextView
    private lateinit var tvproprice: TextView
    private lateinit var add : Button
    private lateinit var buy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_details)

        initView()
        setValuesToViews()

        add.setOnClickListener{

        }

        buy.setOnClickListener{

        }
    }

    private fun initView() {
        tvproID = findViewById(R.id.tvproId)
        tvproname = findViewById(R.id.tvproname)
        tvprocat = findViewById(R.id.tvprocat)
        tvinnoname = findViewById(R.id.tvinnoname)
        tvadddes = findViewById(R.id.tvadddes)
        tvproprice = findViewById(R.id.tvproprice)

        add = findViewById(R.id.add)
        buy = findViewById(R.id.buy)
    }

    private fun setValuesToViews(){
        tvproID.text = intent.getStringExtra("proId")
        tvproname.text = intent.getStringExtra("proname")
        tvprocat.text = intent.getStringExtra("procat")
        tvinnoname.text = intent.getStringExtra("innoname")
        tvadddes.text = intent.getStringExtra("adddes")
        tvproprice.text = intent.getStringExtra("proprice")
    }

}