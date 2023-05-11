package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.models.ProductModel
import com.example.madproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productCategory: EditText
    private lateinit var innovatorName: EditText
    private lateinit var additionalDescription: EditText
    private lateinit var productPrice: EditText
    private lateinit var productImage: Button
    private lateinit var btnSubmit: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productName = findViewById(R.id.pname)
        productCategory = findViewById(R.id.pcat)
        innovatorName = findViewById(R.id.iname)
        additionalDescription = findViewById(R.id.ades)
        productPrice = findViewById(R.id.pprice)
        productImage = findViewById(R.id.upics)
        btnSubmit = findViewById(R.id.submit)

        dbRef = FirebaseDatabase.getInstance().getReference("Products")

        btnSubmit.setOnClickListener{
            saveProductData()
            val intent = Intent(this, Product::class.java)
            startActivity(intent)
        }
    }

    private fun saveProductData(){
        val proname = productName.text.toString()
        val procat = productCategory.text.toString()
        val innoname = innovatorName.text.toString()
        val adddes = additionalDescription.text.toString()
        val proprice = productPrice.text.toString()

        if(proname.isEmpty()){
            productName.error = "Please enter Product name"
        }
        if(procat.isEmpty()){
            productCategory.error = "Please enter Product category"
        }
        if(innoname.isEmpty()){
            innovatorName.error = "Please enter Innovator name"
        }
        if(adddes.isEmpty()){
            additionalDescription.error = "Please enter Additional description"
        }
        if(proprice.isEmpty()){
            productPrice.error = "Please enter Product price"
        }

        val proId = dbRef.push().key!!

        val product = ProductModel(proId, proname, procat, innoname, adddes, proprice)

        dbRef.child(proId).setValue(product)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{ err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}