package com.example.mymad.Models.Validations

class editBuyerFormValidate(private var name: String,
                            private var address: String,
                            private var contactNo: String) {

    fun validateName(): ValidationResult {
        return if(name.isEmpty()){
            ValidationResult.Empty("Name should not be empty")
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

    fun validateContactNo(): ValidationResult {
        return if(contactNo.isEmpty()){
            ValidationResult.Empty("Contact number should not be empty")
        } else if(contactNo.length != 10) {
            ValidationResult.Invalid("Enter a valid contact number")
        } else {
            ValidationResult.Valid
        }
    }
}