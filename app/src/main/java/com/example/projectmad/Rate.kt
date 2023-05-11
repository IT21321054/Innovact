package com.example.projectmad;

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

public class Rate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home3)
        val db = Firebase.firestore

        var id = intent.getStringExtra("id")
//        var rate = intent.getDoubleExtra("rating" , 0.0)
//        var ra = rate.toFloat()

        var submit: Button = findViewById(R.id.button9)
        var rating: RatingBar = findViewById(R.id.ratingBar)
        var wishlist: ImageView = findViewById(R.id.imageView17)
        var home: ImageView = findViewById(R.id.imageView12)

        var rate = 0.0
        db.collection("products").document(id.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data!!["rating"]}")
                    rate = document.data!!["rating"]?.toString()?.toDouble() ?: 0.0
                    rating.rating = rate.toFloat()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        rating.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, r, _ ->
            Log.d("TAG", "rating: $r")
            rate = (rate?.plus(r))?.div(2)!!
        }

        submit.setOnClickListener {
            db.collection("products").document(id.toString())
                .update("rating", rate)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
                rating.rating = rate.toFloat()
                    Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show()}
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


        }

        wishlist.setOnClickListener {
            var intent  = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }

        home.setOnClickListener {
            finish()
        }
    }
}
