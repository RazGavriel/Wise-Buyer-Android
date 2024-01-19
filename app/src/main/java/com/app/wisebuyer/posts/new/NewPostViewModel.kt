package com.app.wisebuyer.posts.new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class NewPostViewModel : ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val db = FirebaseFirestore.getInstance()

    fun createNewPost(post: Post) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            currentUser.email?.let {
                _requestStatus.value = RequestStatus.IN_PROGRESS
                savePost(it, post)
            }
        }
    }

    private fun savePost(userEmail: String, post: Post) {
        val gson = Gson()
        val postJson = gson.toJson(post)

        db.collection("Posts").document(userEmail)
            .set(gson.fromJson(postJson, Map::class.java))
            .addOnSuccessListener {
                _requestStatus.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener {
                _requestStatus.value = RequestStatus.FAILURE
            }
    }

     fun clear() {
        _requestStatus.value = RequestStatus.IDLE
    }
}
