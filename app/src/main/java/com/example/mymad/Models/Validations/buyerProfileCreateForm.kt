package com.example.mymad.Models.Validations

class buyerProfileCreateForm(private var name: String,
                             private var email: String,
                             private var phone: String,
                             private var address: String,
                             private var pwd: String,
                             private var confPwd: String,
) {


    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun validateName(): ValidationResult {
        return if(name.isEmpty()){
            ValidationResult.Empty("Name should not be empty")
        } else {
            ValidationResult.Valid
        }
    }

    fun validateEmail(): ValidationResult {
        return if(email.isEmpty()){
            ValidationResult.Empty("Email address should not be empty")
        } else if(!email.matches(emailPattern.toRegex())) {
            ValidationResult.Invalid("Email format is wrong")
        } else {
            ValidationResult.Valid
        }
    }

    fun validateContactNo(): ValidationResult {
        return if(phone.isEmpty()){
            ValidationResult.Empty("Contact number should not be empty")
        } else if(phone.length != 10) {
            ValidationResult.Invalid("Enter a valid contact number")
        } else {
            ValidationResult.Valid
        }
    }

    fun validateAddress(): ValidationResult {
        return if(address.isEmpty()){
            ValidationResult.Empty("Address should not be empty")
        } else {
            ValidationResult.Valid
        }
    }

    fun validatePwd(): ValidationResult {
        return if(pwd.isEmpty()){
            ValidationResult.Empty("Password should not be empty")
        } else if(pwd.length < 6) {
            ValidationResult.Invalid("Password should have least 6 characters")
        } else {
            ValidationResult.Valid
        }
    }

    fun validateConfPwd(): ValidationResult {
        return if(confPwd.isEmpty()){
            ValidationResult.Empty("Please type your password again")
        } else if(confPwd != pwd) {
            ValidationResult.Invalid("Passwords do not match")
        } else {
            ValidationResult.Valid
        }
    }




}