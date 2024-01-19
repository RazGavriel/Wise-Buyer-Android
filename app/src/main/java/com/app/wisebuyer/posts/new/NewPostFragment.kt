package com.app.wisebuyer.posts.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.wisebuyer.R
import com.app.wisebuyer.utils.RequestStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class NewPostFragment : Fragment() {

    private val newPostViewModel: NewPostViewModel by activityViewModels()

    private lateinit var view: View
    private lateinit var title: TextInputEditText
    private lateinit var type: AutoCompleteTextView
    private lateinit var description: TextInputEditText
    private lateinit var link: TextInputEditText
    private lateinit var price: TextInputEditText
    private lateinit var attachPicture: ImageButton
    private lateinit var submit: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(
            R.layout.fragment_new_post, container, false
        )

        initViews(view)
        handleSubmitButton()
        observeCreatePostStatus()

        return view
    }

    private fun initViews(view: View) {
        title = view.findViewById(R.id.post_title)
        type = view.findViewById(R.id.post_type)
        description = view.findViewById(R.id.post_description)
        link = view.findViewById(R.id.post_link)
        price = view.findViewById(R.id.post_price)
        attachPicture = view.findViewById(R.id.post_attach_picture_button)
        submit = view.findViewById(R.id.post_submit)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ProductType.values().map { it.type }
        )

        type.setAdapter(adapter)
    }

    private fun handleSubmitButton() {
        submit.setOnClickListener {
            // TODO: add validations
            createNewPost()
        }
    }

    private fun createNewPost() {
        val newPost = Post(
            title.text.toString(),
            ProductType.fromString(type.text.toString()),
            description.text.toString(),
            link.text.toString(),
            price.text.toString(),
            ""
        )
        newPostViewModel.createNewPost(newPost)
    }

    private fun observeCreatePostStatus() {
        newPostViewModel.requestStatus.observe(viewLifecycleOwner) { status: RequestStatus ->
            when(status) {
                RequestStatus.IN_PROGRESS ->
                    view.visibility = View.GONE
                RequestStatus.SUCCESS ->
                    findNavController().popBackStack()

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newPostViewModel.clear()
    }
}
