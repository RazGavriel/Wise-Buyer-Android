package com.app.wisebuyer.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.R
import com.app.wisebuyer.posts.LikeRequestStatus
import com.app.wisebuyer.posts.Post
import com.app.wisebuyer.posts.PostCardsAdapter
import com.app.wisebuyer.posts.PostViewModel
import com.app.wisebuyer.utils.RequestStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

abstract class PostBaseFragment : Fragment(), PostCardsAdapter.OnPostItemClickListener {
    lateinit var sharedViewModel: SharedViewModel
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutResourceId(), container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return view
    }

    abstract fun getLayoutResourceId(): Int

    fun observePostViewModel(postViewModel: PostViewModel, recyclerView: RecyclerView) {
        postViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            val postCardsAdapter = PostCardsAdapter(posts)
            postCardsAdapter.setOnPostItemClickListener(this)
            recyclerView.adapter = postCardsAdapter
        }
    }

    fun observeRequestStatus(postViewModel: PostViewModel) {
        postViewModel.requestStatus.observe(viewLifecycleOwner) { result ->
            if (result == RequestStatus.FAILURE) {
                showDialogResponse("Something went wrong while processing your request. " +
                        "Please try again later.")
            }
        }
    }

    fun observeLikeRequestStatus(postViewModel: PostViewModel) {
        postViewModel.likeRequestStatus.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                handleUIAfterLike(result)
            } else {
                showDialogResponse("Something went wrong while processing your request. " +
                        "Please try again later.")
            }
        }
    }

    private fun handleUIAfterLike(result: LikeRequestStatus) {
        result.holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_blank)
        result.holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_blank)
        when (sharedViewModel.userMetaData.email) {
            in result.thumbsUpUsers -> {
                result.holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_filled)
            }
            in result.thumbsDownUsers -> {
                result.holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_filled)
            }
        }
        if (result.postEmail == sharedViewModel.userMetaData.email){
            result.holder.deleteCardButton.visibility = View.VISIBLE }
        else{ result.holder.deleteCardButton.visibility = View.GONE }
    }

    open fun showDialogResponse(message: String) {
        val rootView: View = requireView()
        val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        val snackBarView: View = snackBar.view
        snackBarView.setBackgroundColor(resources.getColor(R.color.black))
        val textView: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(resources.getColor(R.color.white)) // Set your desired text color
        snackBar.show()
    }

    override fun onPostItemClicked(postId: String, postEmail: String,
        holder: PostCardsAdapter.PostViewHolder, mode: String)
    {
        if (mode == "ThumbsUp" || mode == "ThumbsDown") {
            postViewModel.updateThumbsData(postId = postId, mode = mode,
                holder = holder, userEmail = sharedViewModel.userMetaData.email)
        }
        else if (mode == "DeleteCard")
        {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteCardHandler(postId)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun deleteCardHandler(postId: String){
        postViewModel.deletePost(postId)
        val currentFragment = this.toString().split("{")[0]
        if (currentFragment == "PostsFragment") {
            postViewModel.getPosts("", "")
        } else if (currentFragment == "ProfileFragment") {
            postViewModel.getPosts("userEmail", sharedViewModel.userMetaData.email)
        }
    }
}