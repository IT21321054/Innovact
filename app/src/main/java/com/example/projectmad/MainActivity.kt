package com.example.projectmad

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.start

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val textViewMultiLine: TextView = findViewById(R.id.TextViewMultiLine)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val textView: TextView = findViewById(R.id.textView)
        val textView2: TextView = findViewById(R.id.textView2)
        val button6: Button = findViewById(R.id.button6)
        val wishlist: ImageView = findViewById(R.id.imageView2)

        var product: Product? = null

        val db = Firebase.firestore
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    product = Product(document.id,document.data["name"].toString(),document.data["description"].toString(),document.data["price"].toString().toDouble(), document.data["rating"].toString().toDouble())
                    textView.text = product!!.name
                    textView2.text = "$ "+ product!!.price.toString()
                    textViewMultiLine.text = product!!.description
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        val lists = Lists

        db.collection("wishlist")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    product = Product(document.id,document.data["name"].toString(),document.data["description"].toString(),document.data["price"].toString().toDouble(), document.data["rating"].toString().toDouble())
                    lists.addProduct(Prod(product!!.id, product!!.name,java.util.Date()))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }




        product?.let { Log.d("TAG", product!!.name) }
        Log.d("TAG", "Here")


        wishlist.setOnClickListener{
            val intent = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }

        button6.setOnClickListener {
            product?.let { it1 -> Prod(it1.id, product!!.name,java.util.Date()) }
                ?.let { it2 -> lists.addProduct(it2) }
            product?.let { it1 -> db.collection("wishlist").document(it1.id).set(product!!) }
            Toast.makeText(applicationContext,"Added to the wishlist",Toast.LENGTH_SHORT).show()
        }

        button9.setOnClickListener {
            val intent = Intent(this, Interested::class.java)
            startActivity(intent)
        }

        button8.setOnClickListener {
            val intent = Intent(this, Rate::class.java)
            intent.putExtra("id", product!!.id)
            startActivity(intent)
        }


    }
}