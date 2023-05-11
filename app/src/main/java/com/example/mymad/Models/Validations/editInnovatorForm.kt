package com.example.mymad.Models.Validations

class editInnovatorForm(
    private var name: String,
    private var nic: String,
    private var contactNo: String
    ) {

    fun validateName(): ValidationResult {
        return if(name.isEmpty()){
            ValidationResult.Empty("Name should not be empty")
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


}