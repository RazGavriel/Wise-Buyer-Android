package com.app.wisebuyer.posts.edit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.posts.Post
import com.app.wisebuyer.posts.ProductType
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson

class EditPostViewModel : ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post

    private val db = FirebaseFirestore.getInstance()

    private val storage = FirebaseStorage.getInstance()

    fun updatePost(postId: String, post: Post, attachedPictureUri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val imageRefLocation = "productPicture/${currentUser?.uid}/${attachedPictureUri.lastPathSegment}"
        val imageRef: StorageReference = storage.getReference(imageRefLocation)

        if (attachedPictureUri.toString().contains("productPicture/")) {
            if (currentUser != null) {
                currentUser.email?.let {
                    post.userEmail = it
                    post.productPicture = attachedPictureUri.toString()
                    updatePostById(post)
                }
            } else {
                _requestStatus.value = RequestStatus.FAILURE
            }
        } else {
            imageRef.putFile(attachedPictureUri)
                .addOnSuccessListener {
                    if (currentUser != null) {
                        currentUser.email?.let {
                            post.userEmail = it
                            post.productPicture = imageRef.path
                            updatePostById(post)
                        }
                    } else {
                        _requestStatus.value = RequestStatus.FAILURE
                    }
                }
                .addOnFailureListener {
                    _requestStatus.value = RequestStatus.FAILURE
                }

            _requestStatus.value = RequestStatus.IN_PROGRESS
        }
    }

    private fun updatePostById(updatedPost: Post) {
        val postMap = mapOf(
            "title" to updatedPost.title,
            "userEmail" to updatedPost.userEmail,
            "productPicture" to updatedPost.productPicture,
            "productType" to updatedPost.productType,
            "link" to updatedPost.link,
            "price" to updatedPost.price,
            "lastUpdate" to updatedPost.lastUpdate
        )

        db.collection("Posts")
            .document(updatedPost.id)
            .update(postMap)
            .addOnSuccessListener {
                _requestStatus.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener {
                _requestStatus.value = RequestStatus.FAILURE
            }
    }

    fun getPostById(postId: String) {
        db.collection("Posts")
            .document(postId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val postData = documentSnapshot.toObject(Post::class.java)
                    _post.value = postData!!
                }
            }.addOnFailureListener {
                Log.v("APP", "FAILED")
            }
    }

    fun clear() {
        _requestStatus.value = RequestStatus.IDLE
    }
}
