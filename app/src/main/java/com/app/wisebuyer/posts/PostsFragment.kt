package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsFragment : Fragment() {
    private lateinit var addNewPostButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_posts, container, false
        )
        addNewPostButton = view.findViewById<FloatingActionButton>(R.id.add_new_post_button)
        addNewPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_newPostFragment)
        }
        return view
    }
}
