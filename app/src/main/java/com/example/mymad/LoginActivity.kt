package com.example.mymad

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymad.Models.UserModel
import com.example.mymad.databinding.ActivityInnovatorProfileCreateBinding
import com.example.mymad.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initializing auth
        auth = FirebaseAuth.getInstance()

        //redirect user to profile activity if user is already logged in
        /*if ( auth.currentUser != null ){
            intent = Intent(applicationContext, MyProfile::class.java)
            startActivity(intent)
        }*/

        databaseReference = FirebaseDatabase.getInstance().getReference("users")



        binding.loginBtn.setOnClickListener {
            val email = binding.username.text.toString()
            val password = binding.Password.text.toString()

            //checking if the input fields are empty
            if(email.isEmpty() || password.isEmpty()){

                if(email.isEmpty()){
                    binding.username.error = "Enter your email address"
                }
                if(password.isEmpty()){
                    binding.Password.error = "Enter your password"
                }

                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()

            } else if (!email.matches(emailPattern.toRegex())){
                //validate email pattern
                binding.username.error = "Enter a valid email address"

                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()

            } else if (password.length < 6){
                //validate passwords
                binding.Password.error = "Password must be at least 6 characters."

                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()

            } else{
                //Log in
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        //get user and check user type
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
                    }else{
                        Toast.makeText(this, "Username or Password is Wrong. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}