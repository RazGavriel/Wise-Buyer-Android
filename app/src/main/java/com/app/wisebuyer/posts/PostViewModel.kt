package com.app.wisebuyer.posts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class PostViewModel : ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()
    private val _posts = MutableLiveData<List<Post>>()

    val posts: LiveData<List<Post>> get() = _posts
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun getAllPosts() {
        db.collection("Posts").orderBy("createdAt", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                Log.v("APP", documents.toString())
                val postList = documents.toObjects(Post::class.java)
                _posts.value = postList
                _requestStatus.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener { exception ->
                _requestStatus.value = RequestStatus.FAILURE
            }
    }

    fun clear() {
        _requestStatus.value = RequestStatus.IDLE
    }
}
