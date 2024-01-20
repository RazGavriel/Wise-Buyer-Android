package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.wisebuyer.shared.SharedViewModel

class PostsFragment : Fragment() {
    private lateinit var searchInput: EditText
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.fragment_posts, container, false
        )
        searchInput = view.findViewById<EditText>(R.id.search_input)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return view
    }
}
