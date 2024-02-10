package com.app.wisebuyer.posts
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.set
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.wisebuyer.R
import com.app.wisebuyer.shared.PostBaseFragment
import com.google.android.material.textfield.TextInputEditText

class PostsFragment : PostBaseFragment(), PostCardsAdapter.OnPostItemClickListener {

    private val postViewModel: PostViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: TextInputEditText

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
        setupSwipeRefresh()
        checkInitializationShareViewModel()
        observeSearchPost()

        observePostViewModel()
        observeRequestStatus()
        observeLikeRequestStatus()
        observeInitializeUserDataStatus()

        postViewModel.getPosts("", "")
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        searchInput = view.findViewById(R.id.search_input)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeSearchPost() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                observePostViewModel(postViewModel, recyclerView, editable.toString())
            }
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }
        });
    }

    private fun setupSwipeRefresh(){
        swipeRefreshLayout.setOnRefreshListener {
            searchInput.text = Editable.Factory.getInstance().newEditable("")
            postViewModel.getPosts("", "")
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observePostViewModel() {
        observePostViewModel(postViewModel, recyclerView,null)
    }

    private fun observeRequestStatus() {
        observeRequestStatus(postViewModel)
    }

    private fun observeLikeRequestStatus() {
        observeLikeRequestStatus(postViewModel)
    }

    private fun observeInitializeUserDataStatus() {
        observeInitializeUserDataStatus(postViewModel)
    }
}
