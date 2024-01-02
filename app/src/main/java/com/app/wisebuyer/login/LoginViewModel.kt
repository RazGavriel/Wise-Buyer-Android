package com.app.wisebuyer.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginViewModel: ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private lateinit var auth : FirebaseAuth

    fun loginUser(credentials: UserCredentials) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(credentials.email, credentials.password).addOnCompleteListener { task ->
            _loginResult.value = task.isSuccessful
        }
    }
}