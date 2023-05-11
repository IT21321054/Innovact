package com.example.mymad

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mymad.Models.UserModel
import com.example.mymad.Models.Validations.ValidationResult
import com.example.mymad.Models.Validations.buyerProfileCreateForm
import com.example.mymad.databinding.ActivityCreateBuyerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateBuyer : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBuyerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: FirebaseDatabase
    private lateinit var storageReference: FirebaseStorage
    private lateinit var uri: Uri
    private var validCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing storage reference
        storageReference = FirebaseStorage.getInstance()

        //image uri implementation
        val imageView = binding.ProfileDp
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            }
        )

        binding.CardView.setOnClickListener {
            galleryImage.launch("image/*")
        }

        //Initializing auth and database variables
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()




        binding.buttonSave.setOnClickListener {
            //Toast.makeText(this, "Yooo", Toast.LENGTH_SHORT).show()

            var name = binding.buyerFullName.text.toString()
            var email = binding.buyerEmail.text.toString()
            var phone = binding.etBuyerPhone.text.toString()
            var address = binding.etBuyerAddress.text.toString()
            var pwd = binding.etPwd.text.toString()
            var confPwd = binding.etComPwd.text.toString()

            validate(name, email, phone, address, pwd, confPwd)
            //Toast.makeText(this, validCount, Toast.LENGTH_SHORT).show()


            if (validCount == 6) {
                //Toast.makeText(this, "Val passed", Toast.LENGTH_SHORT).show()
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {

                    if (it.isSuccessful){
                        //save user in the db
                        var dbRef =  databaseReference.reference.child("users").child(auth.currentUser!!.uid)
                        val user = UserModel( name, email,"",phone,"","","",auth.currentUser!!.uid,"Buyer",address)

                        dbRef.setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                //Upload profile picture to firebase storage
                                uploadImage()

                                //redirect user to login activity
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Something went wrong, try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

                }

            }

        }






    }

    private fun uploadImage() {
        //set a default image to upload if user have not selected a image
        if(!this::uri.isInitialized){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.ic_baseline_account_circle_24}")
        }


        storageReference.getReference("Users").child(auth.currentUser!!.uid)
            .putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload the image.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun validate (name: String, email: String,phone: String,address: String,pwd: String,conPwd: String) {
        validCount = 0 // reset value

        //create a class object to validate user entered data
        var form = buyerProfileCreateForm(name, email, phone, address, pwd, conPwd)

        var nameValidity = form.validateName()
        var emailValidity = form.validateEmail()
        var phoneNoValidity = form.validateContactNo()
        var addressValidity = form.validateAddress()
        var pwdValidity = form.validatePwd()
        var confPwdValidity = form.validateConfPwd()


        when (nameValidity) {
            is ValidationResult.Empty -> {
                binding.buyerFullName.error = nameValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {}
        }

        when (emailValidity) {
            is ValidationResult.Empty -> {
                binding.buyerEmail.error = emailValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.buyerEmail.error = emailValidity.errorMessage
            }
        }

        when (phoneNoValidity) {
            is ValidationResult.Empty -> {
                binding.etBuyerPhone.error = phoneNoValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etBuyerPhone.error = phoneNoValidity.errorMessage
            }
        }

        when (addressValidity) {
            is ValidationResult.Empty -> {
                binding.etBuyerAddress.error = addressValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etBuyerAddress.error = addressValidity.errorMessage
            }
        }


        when (pwdValidity) {
            is ValidationResult.Empty -> {
                binding.etPwd.error = pwdValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etPwd.error = pwdValidity.errorMessage
            }
        }

        when (confPwdValidity) {
            is ValidationResult.Empty -> {
                binding.etComPwd.error = confPwdValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etComPwd.error = confPwdValidity.errorMessage
            }
        }


    }

}