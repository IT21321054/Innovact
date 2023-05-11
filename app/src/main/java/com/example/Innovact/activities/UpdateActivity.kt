package com.example.Innovact.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.Innovact.R
import com.example.Innovact.models.TopicModel
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var txtTitle: EditText
    private lateinit var txtDescription: EditText

    private lateinit var btnUpdate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_dialog)
        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener{
            updateTaskData(
                intent.getStringExtra("id").toString(),
                txtTitle.text.toString(),
                txtDescription.text.toString()
            )
        }

    }

    private fun initView() {
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDescription)
        btnUpdate = findViewById(R.id.btnSubmit)

    }
    private fun setValuesToViews() {
        txtTitle.setText(intent.getStringExtra("title"))
        txtDescription.setText(intent.getStringExtra("description"))

    }

    private fun updateTaskData(id: String,title: String,description: String) {

        val dbRef = FirebaseDatabase.getInstance().getReference("Topic").child(id)
        val taskInfo = TopicModel(id,title,description)
        val task = dbRef.setValue(taskInfo)

        task.addOnSuccessListener {
            Toast.makeText(this, "FeedBack data updated", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Update Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}