package com.example.mymad.Models.Validations

class createInovatorValidations(
    private var name: String,
    private var email: String,
    private var nic: String,
    private var contactNo: String,
    private var DOB: String,
    private var pwd: String,
    private var confPwd: String
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

    fun validateNIC(): ValidationResult {
        return if(nic.isEmpty()){
            ValidationResult.Empty("NIC should not be empty")
        } else if ( nic.length != 12){
            ValidationResult.Invalid("NIC should have 12 digits")
        } else {
            ValidationResult.Valid
        }
    }

    fun validateContactNo(): ValidationResult {
        return if(contactNo.isEmpty()){
            ValidationResult.Empty("Contact number should not be empty")
        } else if(contactNo.length != 10) {
            ValidationResult.Invalid("Enter a valid contact number")
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


    fun validateDob(): ValidationResult {
        return if(DOB == "MM / DD / YY"){
            ValidationResult.Empty("Please select your Birthday")
        } else {
            ValidationResult.Valid
        }
    }





}