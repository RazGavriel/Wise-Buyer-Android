package com.app.wisebuyer.posts.new

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.posts.*
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage


class NewPostViewModel: ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = Firebase.storage

    fun createNewPost(post:Post) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        Log.v("APP", currentUser.toString())
        if (currentUser != null) {
            currentUser.email?.let {
                _requestStatus.value = RequestStatus.IN_PROGRESS
                db.collection("Posts").document(it)
                    .set(post)
                    .addOnSuccessListener {
                        _requestStatus.value = RequestStatus.SUCCESS
                    }
                    .addOnFailureListener {
                        _requestStatus.value = RequestStatus.FAILURE
                    }
            }
        }
    }


}