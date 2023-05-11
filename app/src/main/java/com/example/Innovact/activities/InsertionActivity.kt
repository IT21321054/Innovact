package com.example.Innovact.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.Innovact.models.TopicModel
import com.example.Innovact.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var txtTitle: EditText
    private lateinit var txtDescription: EditText


    private lateinit var btnSave: TextView

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        txtTitle= findViewById(R.id.txtTitle)
        txtDescription= findViewById(R.id.txtDescription)

        btnSave= findViewById(R.id.btnSubmit)

        dbRef = FirebaseDatabase.getInstance().getReference("Topic")

        btnSave.setOnClickListener{
            saveTaskData()
        }
    }

    private fun saveTaskData() {
        //geting values
        val title = txtTitle.text.toString()
        val description = txtDescription.text.toString()


        if(title.isEmpty()){
            txtTitle.error="Please enter Title"
        }
        if(description.isEmpty()){
            txtDescription.error="Please enter Description"
        }



        val id = dbRef.push().key!!

        val task= TopicModel(id,title,description)


        dbRef.child(id).setValue(task)
            .addOnCompleteListener{
                Toast.makeText(this,"Data insert successfully", Toast.LENGTH_LONG).show()
                txtTitle.text.clear()
                txtDescription.text.clear()

            }.addOnFailureListener { err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}