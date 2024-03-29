package com.app.wisebuyer.shared

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.MainActivity
import com.app.wisebuyer.R
import com.app.wisebuyer.posts.LikeRequestStatus
import com.app.wisebuyer.posts.Post
import com.app.wisebuyer.posts.PostCardsAdapter
import com.app.wisebuyer.posts.PostViewModel
import com.app.wisebuyer.profile.UserMetaData
import com.app.wisebuyer.singup.UserProperties
import com.app.wisebuyer.utils.RequestStatus
import com.app.wisebuyer.utils.closeKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.app.wisebuyer.utils.showDialogResponse

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

    fun observePostViewModel(postViewModel: PostViewModel, recyclerView: RecyclerView, query: String?) {
        postViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            var displayedPost: List<Post> = posts;
            if(query != null && query != "") {
                displayedPost = posts.filter { post ->
                    post.title.contains(query, ignoreCase = true)
                }
            }

            val postCardsAdapter = PostCardsAdapter(displayedPost)
            postCardsAdapter.setOnPostItemClickListener(this)
            recyclerView.adapter = postCardsAdapter
            closeKeyboard(requireContext(), requireView())
        }
    }

    fun observeRequestStatus(postViewModel: PostViewModel) {
        postViewModel.requestStatus.observe(viewLifecycleOwner) { result ->
            if (result == RequestStatus.FAILURE) {
                showDialogResponse("Something went wrong while processing your request. " +
                        "Please try again later.", requireView())
            }
        }
    }

    fun observeLikeRequestStatus(postViewModel: PostViewModel) {
        postViewModel.likeRequestStatus.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                handleUIAfterLike(result)
            } else {
                showDialogResponse("Something went wrong while processing your request. " +
                        "Please try again later.", requireView())
            }
        }
    }

    fun observeInitializeUserDataStatus(postViewModel: PostViewModel) {
        postViewModel.initializeUserDataStatus.observe(viewLifecycleOwner) { result: UserMetaData? ->
            if (result!!.email != ""){
                sharedViewModel.userMetaData = result
                updateHeaderNavigationDrawer()
            }
            else{
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    fun updateHeaderNavigationDrawer(){
        (activity as MainActivity).updateHeaderUserName(UserProperties(
            sharedViewModel.userMetaData.firstName, sharedViewModel.userMetaData.lastName))
    }

    private fun handleUIAfterLike(result: LikeRequestStatus) {
        result.holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_blank)
        result.holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_blank)
        result.holder.textThumbsUp.text = result.thumbsUpUsers.size.toString()
        result.holder.textThumbsDown.text = result.thumbsDownUsers.size.toString()
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
        } else if (mode == "EditCard") {
            val args = Bundle().apply { putString("postId", postId) }
            findNavController().navigate(R.id.editPostFragment, args);
        } else if (mode == "LinkHandler"){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(postId))
            context?.startActivity(browserIntent)
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

    fun checkInitializationShareViewModel(){
        if (sharedViewModel.userMetaData.email == ""){
            postViewModel.getUserMetaData()
        }
    }



}