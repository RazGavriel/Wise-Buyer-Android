package com.app.wisebuyer.posts

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.posts.Post
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson

class PostViewModel : ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()

    private val _posts = MutableLiveData<Post>()

    val posts: LiveData<Post> get() = _posts
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val db = FirebaseFirestore.getInstance()

    private val storage = FirebaseStorage.getInstance()

    fun getAllPosts() {
        val posts = db.collection("Posts").get()

    }

    private fun savePost(post: Post) {

    }

    fun clear() {
        _requestStatus.value = RequestStatus.IDLE
    }
}
