package com.app.wisebuyer.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> get() = _signUpResult
    private lateinit var auth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    fun signUpUser(credentials: UserCredentials, userMetaData: UserMetaData) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val user = returnUserAsJson(userMetaData, credentials.email)
                    db.collection("Users").document(credentials.email)
                        .set(user)
                        .addOnSuccessListener {
                                _signUpResult.value = true
                        }
                    }
                }
        }
    fun clearSignUpResult() {
        _signUpResult.value = false
    }


    private fun returnUserAsJson(userMetaData: UserMetaData, email: String) : MutableMap<String, Any> {
        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = userMetaData.firstName
        user["lastName"] = userMetaData.lastName
        user["profilePhoto"] = 0
        return user
    }
}



