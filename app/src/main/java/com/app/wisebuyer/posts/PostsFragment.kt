package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class PostsFragment : Fragment() {
    private lateinit var searchInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_posts, container, false
        )
        searchInput = view.findViewById<EditText>(R.id.search_input)
        return view
    }
}
