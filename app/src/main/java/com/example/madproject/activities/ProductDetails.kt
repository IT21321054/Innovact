package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.example.madproject.models.ProductModel
import com.google.firebase.database.FirebaseDatabase

class ProductDetails : AppCompatActivity() {

    private lateinit var tvproID : TextView
    private lateinit var tvproname: TextView
    private lateinit var tvprocat: TextView
    private lateinit var tvinnoname: TextView
    private lateinit var tvadddes: TextView
    private lateinit var tvproprice: TextView
    private lateinit var update : Button
    private lateinit var delete : Button
    private lateinit var home: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        initView()
        setValuesToViews()

        update.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("proId").toString(),
                intent.getStringExtra("proname").toString(),
                intent.getStringExtra("procat").toString(),
                intent.getStringExtra("adddes").toString(),
                intent.getStringExtra("proprice").toString(),
            )
        }

        delete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("proId").toString()
            )
        }

        home.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun initView(){
        tvproID = findViewById(R.id.tvproId)
        tvproname = findViewById(R.id.tvproname)
        tvprocat = findViewById(R.id.tvprocat)
        tvinnoname = findViewById(R.id.tvinnoname)
        tvadddes = findViewById(R.id.tvadddes)
        tvproprice = findViewById(R.id.tvproprice)

        update = findViewById(R.id.update)
        delete = findViewById(R.id.delete)
        home = findViewById(R.id.home)
    }

    private fun setValuesToViews(){
        tvproID.text = intent.getStringExtra("proId")
        tvproname.text = intent.getStringExtra("proname")
        tvprocat.text = intent.getStringExtra("procat")
        tvinnoname.text = intent.getStringExtra("innoname")
        tvadddes.text = intent.getStringExtra("adddes")
        tvproprice.text = intent.getStringExtra("proprice")
    }

    private fun openUpdateDialog(
        proId: String,
        proname: String,
        procat: String,
        adddes: String,
        proprice: String
    ){
        val pDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val pDialogView = inflater.inflate(R.layout.update_dialog, null)

        pDialog.setView(pDialogView)

        val pname = pDialogView.findViewById<EditText>(R.id.pname)
        val pcat = pDialogView.findViewById<EditText>(R.id.pcat)
        val ades = pDialogView.findViewById<EditText>(R.id.ades)
        val pprice = pDialogView.findViewById<EditText>(R.id.pprice)
        val update = pDialogView.findViewById<Button>(R.id.update)

        pname.setText(intent.getStringExtra("proname").toString())
        pcat.setText(intent.getStringExtra("procat").toString())
        ades.setText(intent.getStringExtra("adddes").toString())
        pprice.setText(intent.getStringExtra("proprice").toString())

        pDialog.setTitle("Updating $proname Record")

        val alertDialog = pDialog.create()
        alertDialog.show()

        update.setOnClickListener{
            updateProductData(
                proId,
                pname.text.toString(),
                pcat.text.toString(),
                ades.text.toString(),
                pprice.text.toString()
            )

            Toast.makeText(applicationContext, "Product Data Updated", Toast.LENGTH_LONG).show()

            //We are setting updated data to our textviews
            tvproname.text = pname.text.toString()
            tvprocat.text = pcat.text.toString()
            tvadddes.text = ades.text.toString()
            tvproprice.text = pprice.text.toString()

            alertDialog.dismiss()
        }

    }

    private fun updateProductData(
        id: String,
        name: String,
        category: String,
        details: String,
        price: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Products").child(id)
        val proInfo = ProductModel(id,name,category,details,price)
        dbRef.setValue(proInfo)
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Products").child(id)
        val pTask = dbRef.removeValue()

        pTask.addOnSuccessListener {
            Toast.makeText(this,"Product Data Deleted.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, Product::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error->
            Toast.makeText(this,"Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}