package com.example.mymad

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mymad.Models.UserModel
import com.example.mymad.Models.Validations.ValidationResult
import com.example.mymad.Models.Validations.editBuyerFormValidate
import com.example.mymad.Models.Validations.editInnovatorForm
import com.example.mymad.databinding.ActivityInnovatorProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class Innovator_Profile_Edit : AppCompatActivity() {

    private lateinit var binding: ActivityInnovatorProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var storageRef: FirebaseStorage
    private lateinit var uid: String
    private lateinit var uri: Uri
    private var validCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInnovatorProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                binding.inputNIC.setText(user.nic)
                binding.etContactNo.setText(user.contactNo)
                binding.etPrevWork.setText(user.prevWork)
                getUserProfilePicture()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.save.setOnClickListener {


            var name = binding.etName.text.toString()
            var nic = binding.inputNIC.text.toString()
            var phone = binding.etContactNo.text.toString()
            var prevWork = binding.etPrevWork.text.toString()

            validateForm(name,nic,phone)
            if (validCount == 3) {
                //Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show()
                val map = HashMap<String,Any>()

                //add data to hashMap
                map["name"] = name
                map["nic"] = nic
                map["contactNo"] = phone
                map["prevWork"] = prevWork


                //update database from hashMap
                databaseReference.child(uid).updateChildren(map).addOnCompleteListener {
                    if( it.isSuccessful){
                        intent = Intent(applicationContext, MyProfile::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                //upload image if user has selected a new image
                if(this::uri.isInitialized){
                    uploadImage()
                }
            }
        }
    }

    private fun validateForm(name: String, nic: String, phone: String) {
        validCount = 0 // reset value
        var form = editInnovatorForm(name, nic, phone)

        var nameValidity = form.validateName()
        var phoneNoValidity = form.validateContactNo()
        var nicValidity = form.validateNIC()

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
                binding.etContactNo.error = phoneNoValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.etContactNo.error = phoneNoValidity.errorMessage
            }
        }

        when (nicValidity) {
            is ValidationResult.Empty -> {
                binding.inputNIC.error = nicValidity.errorMessage
            }
            is ValidationResult.Valid -> {
                validCount++
            }
            is ValidationResult.Invalid -> {
                binding.inputNIC.error = nicValidity.errorMessage
            }
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

    private fun uploadImage() {

        storageRef.getReference("Users").child(auth.currentUser!!.uid)
            .putFile(uri).addOnSuccessListener {
                //Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload the image.", Toast.LENGTH_SHORT).show()
            }
    }

}



