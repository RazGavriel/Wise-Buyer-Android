package com.app.wisebuyer.posts.new

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.wisebuyer.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.app.wisebuyer.posts.new.ProductType
import com.app.wisebuyer.utils.RequestStatus

class NewPostFragment : Fragment() {

    private val newPostViewModel: NewPostViewModel by activityViewModels()

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
        val view: View = inflater.inflate(
            R.layout.fragment_new_post, container, false
        )

        title = view.findViewById(R.id.post_title)
        type = view.findViewById(R.id.post_type)
        description = view.findViewById(R.id.post_description)
        link = view.findViewById(R.id.post_link)
        price = view.findViewById(R.id.post_price)
        attachPicture = view.findViewById(R.id.post_attach_picture_button)
        submit = view.findViewById(R.id.post_submit)


        submit.setOnClickListener {
            val newPost = Post(
                title.text.toString(),
                ProductType.valueOf(type.text.toString()),
                description.text.toString(),
                link.text.toString(),
                price.text.toString().toInt(),
                ""
            )
            newPostViewModel.createNewPost(newPost)
        }

        newPostViewModel.requestStatus.observe(viewLifecycleOwner) { status: RequestStatus ->
            Log.v("APP", status.toString())
        }
        return view
    }
}