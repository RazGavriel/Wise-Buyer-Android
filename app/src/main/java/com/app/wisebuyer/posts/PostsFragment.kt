package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsFragment : Fragment() {

    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var addNewPostButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_posts, container, false
        )

        initViews(view)
        setupRecyclerView()
        handleAddNewClick()

        // fetch all posts
        postViewModel.getAllPosts()

        observePostViewModel()

        return view
    }

    private fun initViews(view: View) {
        addNewPostButton = view.findViewById(R.id.add_new_post_button)
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar_posts)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleAddNewClick() {
        addNewPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_newPostFragment)
        }
    }

    private fun observePostViewModel() {
        postViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            Log.v("APP", posts.toString())
            recyclerView.adapter = PostCardsAdapter(posts)
            progressBar.visibility = View.GONE
        }
    }
}
