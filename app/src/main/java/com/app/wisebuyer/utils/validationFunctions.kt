package com.app.wisebuyer.utils

import android.util.Patterns
import android.widget.EditText
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData

fun checkCredentials(credentials: UserCredentials, emailInput: EditText,
                     passwordInput:EditText): Boolean{
    if (credentials.email.isEmpty()){
        emailInput.error = "Enter a email"
    }
    else if (credentials.password.isEmpty()){
        passwordInput.error = "Enter a password"
    }
    else if (credentials.password.length <=6) {
        passwordInput.error = "Password need to more than 6 characters long"
    }
    else if (!Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches()){
        emailInput.error = "Enter valid email format"
    }
    else{ return true }
    return false
}

fun checkMetaData(userMetaData: UserMetaData, firstNameInput: EditText,
                          lastNameInput: EditText): Boolean {
    if (userMetaData.firstName.isEmpty() || !isString(userMetaData.firstName)){
        firstNameInput.error = "Enter valid first name"
    }
    else if (userMetaData.lastName.isEmpty() || !isString(userMetaData.lastName)){
        lastNameInput.error = "Enter valid last name"
    }
    else{ return true }
    return false
}

private fun isString(value: String): Boolean {
    return value.all { it.isLetter() }
}