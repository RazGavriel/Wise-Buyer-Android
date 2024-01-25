package com.app.wisebuyer.posts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.R
import com.app.wisebuyer.shared.PostBaseFragment

class PostsFragment : PostBaseFragment(), PostCardsAdapter.OnPostItemClickListener {

    private val postViewModel: PostViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_posts
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = super.onCreateView(inflater, container, savedInstanceState)
        if (view != null) {
            initViews(view)
        }
        setupRecyclerView()
        observePostViewModel()
        observeRequestStatus()
        observeLikeRequestStatus()

        postViewModel.getPosts("", "")
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePostViewModel() {
        observePostViewModel(postViewModel, recyclerView)
    }

    private fun observeRequestStatus() {
        observeRequestStatus(postViewModel)
    }

    private fun observeLikeRequestStatus() {
        observeLikeRequestStatus(postViewModel)
    }
}
