package com.example.mymad

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mymad.Models.UserModel
import com.example.mymad.Models.Validations.ValidationResult
import com.example.mymad.Models.Validations.editBuyerFormValidate
import com.example.mymad.Models.Validations.editInnovatorForm
import com.example.mymad.databinding.ActivityBuyerProfileEditingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class Buyer_profile_editing : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerProfileEditingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var storageRef: FirebaseStorage
    private lateinit var uid: String
    private lateinit var uri: Uri
    private var validCount = 0

    private var new = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerProfileEditingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get current user id
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

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
        //initializing storage reference
        storageRef = FirebaseStorage.getInstance()

        binding.CardView.setOnClickListener {
            galleryImage.launch("image/*")
        }

        databaseReference.child(auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //retrieve values from the db and convert them to user data class
                var user = snapshot.getValue(UserModel::class.java)!!

                binding.etName.setText(user.name)
                binding.etAddress.setText(user.address)
                binding.etPhone.setText(user.contactNo)
                getUserProfilePicture()
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

        binding.btnUpdate.setOnClickListener {


            var name = binding.etName.text.toString()
            var address = binding.etAddress.text.toString()
            var phone = binding.etPhone.text.toString()

            validateForm(name,address, phone)
            if (validCount == 3) {
                //Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show()

                val map = HashMap<String,Any>()

                //add data to hashMap
                map["name"] = name
                map["address"] = address
                map["contactNo"] = phone


                //update database from hashMap
                databaseReference.child(uid).updateChildren(map).addOnCompleteListener {
                    if( it.isSuccessful){
                        intent = Intent(applicationContext, buyerProfile::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                if(this::uri.isInitialized){
                    uploadImage()
                    // uri = Uri.parse("android.resource://$packageName/${R.drawable.ic_baseline_account_circle_24}")
                }
            }
        }
    }

    private fun validateForm(name: String, address: String, phone: String) {
        validCount = 0 // reset value
        var form = editBuyerFormValidate(name, address, phone)

        var nameValidity = form.validateName()
        var phoneNoValidity = form.validateContactNo()
        var addressValidity = form.validateAddress()

        when (nameValidity) {
            is ValidationResult.Empty -> {
                binding.etName.error = nameValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {}
        }

        when (phoneNoValidity) {
            is ValidationResult.Empty -> {
                binding.etPhone.error = phoneNoValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etPhone.error = phoneNoValidity.errorMessage
            }
        }

        when (addressValidity) {
            is ValidationResult.Empty -> {
                binding.etAddress.error = addressValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etAddress.error = addressValidity.errorMessage
            }
        }
    }

    private fun uploadImage() {

        storageRef.getReference("Users").child(auth.currentUser!!.uid)
            .putFile(uri).addOnSuccessListener {
                //Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload the image.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserProfilePicture() {
        //find image named with the current uid
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")

        //create temporary local file to store the retrieved image
        val localFile = File.createTempFile("tempImage", ".jpg")

        //retrieve image and store it to created temp file
        storageReference.getFile(localFile).addOnSuccessListener {

            //covert temp file to bitmap
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            //bind image
            binding.ProfileDp.setImageBitmap(bitmap)


        }.addOnFailureListener{
            //Toast.makeText(activity, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
        }
    }



}