package com.app.wisebuyer.singup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.utils.generateAvatar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.app.wisebuyer.login.UserCredentials
import kotlinx.coroutines.runBlocking
import java.util.UUID

class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<String>()
    val signUpResult: LiveData<String> get() = _signUpResult
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = Firebase.storage

    fun signUpUser(credentials: UserCredentials, userProperties: UserProperties) {
        auth = Firebase.auth
        val storageReference = storage.reference

        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnSuccessListener {
                val user = returnUserAsJson(userProperties)
                val imageRef = storageReference.child(user["profilePhoto"] as String)

                val avatarBytes = runBlocking {
                    generateAvatar(userProperties.firstName, userProperties.lastName)
                }

                val uploadTask = imageRef.putBytes(avatarBytes)

                uploadTask.addOnSuccessListener {
                    db.collection("Users").document(credentials.email)
                        .set(user)
                        .addOnSuccessListener {
                            _signUpResult.value = "Success"
                        }
                        .addOnFailureListener {
                            _signUpResult.value = "Cannot upload first profile image"
                        }
                }
            }

            .addOnFailureListener {
                _signUpResult.value = "The email is already in use"
            }
    }
    fun clearSignUpResult() {
        _signUpResult.value = ""
    }


    private fun returnUserAsJson(userProperties: UserProperties)
    : MutableMap<String, Any> {
        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = userProperties.firstName.replaceFirstChar(Char::titlecase)
        user["lastName"] = userProperties.lastName.replaceFirstChar(Char::titlecase)
        val randomUuid = UUID.randomUUID().toString()
        user["profilePhoto"] = "profilePictures/$randomUuid.jpg"
        return user
    }
}
