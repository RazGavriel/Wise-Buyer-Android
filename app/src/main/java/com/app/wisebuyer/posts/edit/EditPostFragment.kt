package com.app.wisebuyer.posts.edit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.wisebuyer.R
import com.app.wisebuyer.posts.Post
import com.app.wisebuyer.posts.ProductType
import com.app.wisebuyer.utils.RequestStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditPostFragment : Fragment() {

    private val newPostViewModel: EditPostViewModel by activityViewModels()

    private lateinit var view: View
    private lateinit var title: TextInputEditText
    private lateinit var type: Spinner
    private lateinit var description: TextInputEditText
    private lateinit var link: TextInputEditText
    private lateinit var price: TextInputEditText
    private lateinit var attachPictureButton: ImageButton
    private lateinit var editPostButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var attachedPicture: Uri
    private var selectedProductType : String = ProductType.OTHER.type
    private lateinit var postId: String;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(
            R.layout.fragment_edit_post, container, false
        )

        initViews(view)
        handleUpdatePostButton()
        observeEditPostStatus()
        observeGetPost()
        handleAttachProductPicture()

        postId = arguments?.getString("postId").toString()
        postId?.let { newPostViewModel.getPostById(it) }

        return view
    }

    private fun initViews(view: View) {
        title = view.findViewById(R.id.post_title)
        type = view.findViewById(R.id.post_type)
        description = view.findViewById(R.id.post_description)
        link = view.findViewById(R.id.post_link)
        price = view.findViewById(R.id.post_price)
        attachPictureButton = view.findViewById(R.id.post_attach_picture_button)
        editPostButton = view.findViewById(R.id.post_submit)
        progressBar = view.findViewById(R.id.progress_bar_create_new_post)


        // initialize spinner options
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ProductType.entries.map { it.type }
        )

        type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedProductType = parentView.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedProductType = ProductType.OTHER.type
            }
        }
        type.adapter = adapter
    }

    private fun handleUpdatePostButton() {
        editPostButton.setOnClickListener {
            // TODO: add validations
            updatePost()
        }
    }

    private fun updatePost() {
        val editPost = Post(
            id = postId,
            title = title.text.toString(),
            productType = ProductType.fromString(selectedProductType),
            description = description.text.toString(),
            link = link.text.toString(),
            price = price.text.toString(),
        )
        postId?.let { newPostViewModel.updatePost(it,editPost, attachedPicture) }
    }

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            attachedPicture = it
        }
    }
    private fun handleAttachProductPicture() {
        attachPictureButton.setOnClickListener {
            pickImageContract.launch("image/*")
        }
    }

    private fun observeEditPostStatus() {
        newPostViewModel.requestStatus.observe(viewLifecycleOwner) { status: RequestStatus ->
            when(status) {
                RequestStatus.IN_PROGRESS ->{
                    loading();
                }
                RequestStatus.SUCCESS ->
                    findNavController().popBackStack()

                else -> {}
            }
        }
    }

    private fun observeGetPost() {
        newPostViewModel.post.observe(viewLifecycleOwner) { post: Post ->
            title.setText(post.title)
            description.setText(post.description)
            link.setText(post.link)
            price.setText(post.price)
            attachedPicture = Uri.parse(post.productPicture)
        }
    }

    private fun loading(){
        title.visibility = View.GONE
        type.visibility = View.GONE
        description.visibility = View.GONE
        link.visibility = View.GONE
        price.visibility = View.GONE
        attachPictureButton.visibility = View.GONE
        editPostButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newPostViewModel.clear()
    }
}
