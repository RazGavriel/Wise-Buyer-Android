package com.app.wisebuyer.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.login.UserCredentials
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> get() = _signUpResult

    private lateinit var auth : FirebaseAuth
    fun signUpUser(credentials: UserCredentials) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {task ->
                _signUpResult.value = task.isSuccessful
        }
    }
}