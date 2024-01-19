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
                post.userEmail = it
                savePost(post)
            }
        }
    }

    private fun savePost(post: Post) {
        val gson = Gson()
        val postJson = gson.toJson(post)

        _requestStatus.value = RequestStatus.IN_PROGRESS

        db.collection("Posts")
            .add(gson.fromJson(postJson, Map::class.java))
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
