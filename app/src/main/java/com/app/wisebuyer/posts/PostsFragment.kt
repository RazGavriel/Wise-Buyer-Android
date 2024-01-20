package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsFragment : Fragment() {

    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var addNewPostButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_posts, container, false
        )
        addNewPostButton = view.findViewById<FloatingActionButton>(R.id.add_new_post_button)
        recyclerView = view.findViewById<RecyclerView>(R.id.posts_recycler_view)

        addNewPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_newPostFragment)
        }

        // fetch all posts
        postViewModel.getAllPosts()

        postViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            Log.v("APP", posts.toString())
        }

        return view
    }
}
