package com.app.wisebuyer.utils

import android.util.Patterns
import android.webkit.URLUtil
import android.widget.EditText
import com.app.wisebuyer.login.UserCredentials

fun checkCredentials(
    credentials: UserCredentials, emailInput: EditText,
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

fun isString(value: String): Boolean {
    return value.all { it.isLetter() }
}

fun validatePost(title : String, description :String, link: String,
                 price: String, pictureInitializeStatus: Boolean ): String? {
    if (title.length > 30) {
        return "Title should be less than 30 characters"
    }

    if (description.length > 150) {
        return "Description should not exceed 150 characters"
    }

    if (!URLUtil.isValidUrl(link)) {
        return "Link is not a valid URL"
    }

    if (price.toInt() < 0) {
        return "Price should be a positive integer"
    }

    if (!pictureInitializeStatus)
    {
        return "Picture is missing. Add a picture before proceeding."
    }

    return null
}