package com.app.wisebuyer.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.login.UserCredentials
import com.app.wisebuyer.login.UserMetaData
import com.app.wisebuyer.utils.generateAvatar
import com.app.wisebuyer.utils.uploadBytesImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.runBlocking
import java.util.UUID


class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> get() = _signUpResult
    private lateinit var auth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage = Firebase.storage

    fun signUpUser(credentials: UserCredentials, userMetaData: UserMetaData) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val user = returnUserAsJson(userMetaData, credentials.email)
                    if (user["profilePhoto"] != 0){
                        db.collection("Users").document(credentials.email)
                            .set(user)
                            .addOnSuccessListener {
                                _signUpResult.value = true
                            }
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
        val randomUuid = UUID.randomUUID().toString()
        val avatarBytes = runBlocking{
            generateAvatar(userMetaData.firstName,userMetaData.lastName)
        }
        val isUploaded = uploadBytesImage(storage, randomUuid, avatarBytes)

        if (!isUploaded) {
            user["profilePhoto"] = "profilePictures/$randomUuid.jpg"
        }
        else{ user["profilePhoto"] = 0 }

        return user
    }
}



