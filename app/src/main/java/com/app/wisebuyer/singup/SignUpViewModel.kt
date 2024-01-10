package com.app.wisebuyer.singup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData
import com.app.wisebuyer.utils.generateAvatar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.runBlocking
import java.util.UUID


class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<String>()
    val signUpResult: LiveData<String> get() = _signUpResult
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage = Firebase.storage

    fun signUpUser(credentials: UserCredentials, userMetaData: UserMetaData) {
        auth = Firebase.auth
        val storageReference = storage.getReference()

        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnSuccessListener {
                val user = returnUserAsJson(userMetaData, credentials.email)
                val imageRef = storageReference.child(user["profilePhoto"] as String)

                val avatarBytes = runBlocking {
                    generateAvatar(userMetaData.firstName, userMetaData.lastName)
                }
                val uploadTask = imageRef.putBytes(avatarBytes)

                uploadTask.addOnSuccessListener {
                    db.collection("Users").document(credentials.email)
                        .set(user)
                        .addOnSuccessListener {
                            _signUpResult.value = "Success"
                            Log.w("APP", "created user")
                        }
                        .addOnFailureListener {
                            _signUpResult.value = "Cannot upload first profile image"
                            Log.v("APP", "Cannot upload first profile image")
                        }
                }
            }

            .addOnFailureListener {
                _signUpResult.value = "The email is already in use"
                Log.v("APP", "The email is already in use")
            }
    }
    fun clearSignUpResult() {
        _signUpResult.value = ""
    }

    private fun returnUserAsJson(userMetaData: UserMetaData, email: String)
    : MutableMap<String, Any> {
        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = userMetaData.firstName
        user["lastName"] = userMetaData.lastName
        val randomUuid = UUID.randomUUID().toString()
        user["profilePhoto"] = "profilePictures/$randomUuid.jpg"
        return user
    }
}
