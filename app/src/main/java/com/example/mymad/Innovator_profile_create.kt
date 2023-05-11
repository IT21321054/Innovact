package com.example.mymad

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mymad.Models.UserModel
import com.example.mymad.Models.Validations.ValidationResult
import com.example.mymad.Models.Validations.createInovatorValidations
import com.example.mymad.databinding.ActivityInnovatorProfileCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar


class InnovatorProfileCreate : AppCompatActivity() {
    private lateinit var binding: ActivityInnovatorProfileCreateBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: FirebaseDatabase
    private lateinit var storageReference: FirebaseStorage
    private lateinit var uri: Uri
    private var validCount = 0




    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInnovatorProfileCreateBinding.inflate(layoutInflater)
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


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //date picker
        binding.ivCalendar.setOnClickListener {
            val datepicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // set date to textview
                binding.tvDob.text = "${dayOfMonth} / ${month} / $year" }, year, month, day)

            datepicker.show()
        }

        binding.btnSave.setOnClickListener {
            var name = binding.etName.text.toString()
            var email = binding.editTextTextEmailAddress.text.toString()
            var nic = binding.etNIC.text.toString()
            var contactNo = binding.etContactNo.text.toString()
            var prevWork = binding.etPrevWork.text.toString()
            var gender = binding.genderInput.selectedItem.toString()
            var dob = binding.tvDob.text.toString()
            var pwd = binding.etPwd.text.toString()
            var confPwd = binding.etComPwd.text.toString()


            validateForm(name, email, nic, contactNo, dob, pwd, confPwd)

            if (validCount == 7) {
                //Toast.makeText(this, "Validations passed", Toast.LENGTH_SHORT).show()
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {

                    if (it.isSuccessful){
                        //save user in the db
                       var dbRef =  databaseReference.reference.child("users").child(auth.currentUser!!.uid)
                        val user = UserModel( name, email, nic, contactNo, prevWork, gender, dob, auth.currentUser!!.uid,"Innovator")

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

    private fun validateForm(name: String, email: String, nic: String, contactNo: String, dob: String, pwd: String, confPwd: String) {
        validCount = 0 // reset value

        //create a class object to validate user entered data
        var form = createInovatorValidations(name, email, nic, contactNo, dob, pwd, confPwd)

        val nameValidity = form.validateName()
        var emailValidity = form.validateEmail()
        var nicValidity = form.validateNIC()
        var contactNoValidity = form.validateContactNo()
        var dobValidity = form.validateDob()
        var pwdValidity = form.validatePwd()
        var confPwdValidity = form.validateConfPwd()

        when (nameValidity) {
            is ValidationResult.Empty -> {
                binding.etName.error = nameValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {}
        }

        when (emailValidity) {
            is ValidationResult.Empty -> {
                binding.editTextTextEmailAddress.error = emailValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.editTextTextEmailAddress.error = emailValidity.errorMessage
            }
        }
        when (nicValidity) {
            is ValidationResult.Empty -> {
                binding.etNIC.error = nicValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etNIC.error = nicValidity.errorMessage
            }
        }
        when (contactNoValidity) {
            is ValidationResult.Empty -> {
                binding.etContactNo.error = contactNoValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etContactNo.error = contactNoValidity.errorMessage
            }
        }
        when (dobValidity) {
            is ValidationResult.Empty -> {
                Toast.makeText(this, dobValidity.errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {}
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






