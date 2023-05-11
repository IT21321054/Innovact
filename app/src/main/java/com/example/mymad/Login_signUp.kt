package com.example.mymad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mymad.Models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Login_signUp : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        //redirect user to profile activity if user is already logged in
        if ( auth.currentUser != null ){

            databaseReference.child(auth.currentUser!!.uid).addValueEventListener(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //retrieve values from the db and convert them to user data class
                    var user = snapshot.getValue(UserModel::class.java)!!

                    if( user.type == "Buyer") {
                        intent = Intent(applicationContext, buyerProfile::class.java)
                        startActivity(intent)
                    } else {
                        intent = Intent(applicationContext, MyProfile::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

            /*intent = Intent(applicationContext, MyProfile::class.java)
            startActivity(intent)*/
        }


        val loginButton = findViewById<Button>(R.id.Login)
        loginButton.setOnClickListener {
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        val signUpInnovatorButton = findViewById<Button>(R.id.SignUpAsInnovator)
        signUpInnovatorButton.setOnClickListener {
            intent = Intent(applicationContext, InnovatorProfileCreate::class.java)
            startActivity(intent)
        }

        val signUpBuyerButton = findViewById<Button>(R.id.SignUpAsBuyer)
        signUpBuyerButton.setOnClickListener {
            intent = Intent(applicationContext, CreateBuyer::class.java)
            startActivity(intent)
        }
    }
}

