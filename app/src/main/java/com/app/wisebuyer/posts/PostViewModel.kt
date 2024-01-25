package com.app.wisebuyer.posts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostViewModel : ViewModel() {
    private val _requestStatus = MutableLiveData<RequestStatus>()
    private val _posts = MutableLiveData<List<Post>>()
    private val _likeRequestStatus = MutableLiveData<LikeRequestStatus?>()
    val posts: LiveData<List<Post>> get() = _posts
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus
    val likeRequestStatus: MutableLiveData<LikeRequestStatus?> get() = _likeRequestStatus

    private val db = FirebaseFirestore.getInstance()

    fun getPosts(mode: String, inputFromUser: String) {
        var query: Query = db.collection("Posts")

        if (mode.isNotEmpty() && inputFromUser.isNotEmpty()) {
            query = query.whereEqualTo(mode, inputFromUser)
        }

        query.orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                Log.v("APP", documents.toString())
                val postList = documents.toObjects(Post::class.java)
                _posts.value = postList
                _requestStatus.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener { exception ->
                _requestStatus.value = RequestStatus.FAILURE
            }.addOnSuccessListener {
                Log.v("APP", "GET ALL POSTS")
            }
    }

    fun deletePost(postId: String) {
        val documentReference = db.collection("Posts").document(postId)
        documentReference.delete()
            .addOnSuccessListener {
                _requestStatus.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener { e ->
                _requestStatus.value = RequestStatus.FAILURE
            }.addOnSuccessListener {
                Log.v("APP", "delete POST")
            }
    }

    fun updateThumbsData(postId: String, userEmail: String,
                         holder: PostCardsAdapter.PostViewHolder, mode: String)
    {
        val documentReference = db.collection("Posts").document(postId)
        documentReference.get()
            .addOnSuccessListener { result ->
                val thumbsUpUsers = result.data?.get("thumbsUpUsers") as MutableList<String>
                val thumbsDownUsers = result.data?.get("thumbsDownUsers") as MutableList<String>
                val (finalThumbsUpUsers, finalThumbsDownUsers) =
                    thumbsArrayHandler(mode, userEmail, thumbsUpUsers , thumbsDownUsers)
                documentReference.update("thumbsUpUsers", finalThumbsUpUsers,
                    "thumbsDownUsers", finalThumbsDownUsers)
                val likeRequestStatus = LikeRequestStatus(
                    postEmail = result.data?.get("userEmail").toString(),
                    thumbsUpUsers = finalThumbsUpUsers, thumbsDownUsers = finalThumbsDownUsers,
                    holder = holder)
                _likeRequestStatus.value = likeRequestStatus
            }
            .addOnFailureListener { exception ->
                _likeRequestStatus.value = null
            }
    }

    private fun thumbsArrayHandler(mode: String, userEmail:String, thumbsUpUsers: MutableList<String>,
                                   thumbsDownUsers: MutableList<String>
    ): Pair<MutableList<String>, MutableList<String>> {
        when (mode) {
            "ThumbsUp" -> {
                when (userEmail) {
                    in thumbsUpUsers -> { thumbsUpUsers.remove(userEmail) }
                    in thumbsDownUsers -> {
                        thumbsDownUsers.remove(userEmail)
                        thumbsUpUsers.add(userEmail)
                    }
                    else -> { thumbsUpUsers.add(userEmail) }
                }
            }
            "ThumbsDown" -> {
                when (userEmail) {
                    in thumbsDownUsers -> { thumbsDownUsers.remove(userEmail) }
                    in thumbsUpUsers -> {
                        thumbsUpUsers.remove(userEmail)
                        thumbsDownUsers.add(userEmail)
                    }
                    else -> { thumbsDownUsers.add(userEmail) }
                }
            }
        }
        return Pair(thumbsUpUsers, thumbsDownUsers)
    }
}
