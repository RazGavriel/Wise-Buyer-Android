package com.app.wisebuyer.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.profile.UserMetaData
import com.app.wisebuyer.room.getWiseBuyerLocalDatabase
import com.app.wisebuyer.utils.RequestStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    // init room
    private val localDatabase = getWiseBuyerLocalDatabase(application.applicationContext)

    private val _requestStatus = MutableLiveData<RequestStatus>()
    private val _posts = MutableLiveData<List<Post>>(localDatabase.postDao().getAll())
    private val _likeRequestStatus = MutableLiveData<LikeRequestStatus?>()
    private val _initializeUserDataStatus = MutableLiveData<UserMetaData?>()
    val posts: LiveData<List<Post>> get() = _posts
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus
    val likeRequestStatus: MutableLiveData<LikeRequestStatus?> get() = _likeRequestStatus
    val initializeUserDataStatus: MutableLiveData<UserMetaData?> get() = _initializeUserDataStatus


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

    fun getUserMetaData(){
        val auth = FirebaseAuth.getInstance()
        var newUserMetaData = UserMetaData("","","","")
        val user = auth.currentUser
        val email = user?.email
        if (user != null) {
            db.collection("Users").document(email.toString())
                .get()
                .addOnSuccessListener {
                    newUserMetaData = UserMetaData(email = user.email!!,
                        firstName = it.data?.get("firstName") as String,
                        lastName = it.data!!["lastName"] as String,
                        profilePhoto = it.data!!["profilePhoto"] as String)

                }
                .addOnCompleteListener {
                    initializeUserDataStatus.value = newUserMetaData
                }
        }
    }
}
