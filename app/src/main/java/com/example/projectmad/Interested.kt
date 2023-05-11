package com.example.projectmad

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Interested: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home2)

        var wishlist: ImageView = findViewById(R.id.imageView17)
        var home: ImageView = findViewById(R.id.imageView12)

        wishlist.setOnClickListener {
            val intent = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }

        home.setOnClickListener {
            finish()
        }
    }
}