package com.app.wisebuyer.posts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.R
import com.app.wisebuyer.shared.PostBaseFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.NumberFormat
import java.util.Locale


class PostCardsAdapter(private val posts: List<Post>)
    : RecyclerView.Adapter<PostCardsAdapter.PostViewHolder>() {
    private val storage = FirebaseStorage.getInstance()
    private var onPostItemClickListener: OnPostItemClickListener? = null
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email as String

    interface OnPostItemClickListener {
        fun onPostItemClicked(postId: String, postEmail: String,
          holder: PostViewHolder, mode: String)

    }
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.card_image)
        val title: TextView = itemView.findViewById(R.id.card_title)
        val description: TextView = itemView.findViewById(R.id.card_description)
        val link: TextView = itemView.findViewById(R.id.card_link)
        val price: TextView = itemView.findViewById(R.id.card_price)
        val imageThumbsUp: ImageView = itemView.findViewById(R.id.image_thumbs_up)
        val imageThumbsDown: ImageView = itemView.findViewById(R.id.image_thumbs_down)
        val deleteCardButton: Button = itemView.findViewById(R.id.delete_card_button)
        val editCardButton: Button = itemView.findViewById(R.id.edit_card_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card_item, parent, false)
        return PostViewHolder(view)
    }

    fun setOnPostItemClickListener(listener: PostBaseFragment) {
        this.onPostItemClickListener = listener
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        storage.reference.child(post.productPicture).downloadUrl.addOnSuccessListener {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.image)
        }
        holder.price.text = NumberFormat.getCurrencyInstance(Locale.US).format(post.price.toInt())
        "${post.title} | ${post.productType}".also { holder.title.text = it }
        holder.description.text = post.description
        holder.link.text = post.link
        handleObjectPulling(holder, userEmail, post)
        handleClicksCard(holder, position)
    }

    private fun handleClicksCard(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.imageThumbsUp.setOnClickListener {
            onPostItemClickListener?.onPostItemClicked(post.id, post.userEmail ,
                holder,"ThumbsUp")
        }

        holder.imageThumbsDown.setOnClickListener {
            onPostItemClickListener?.onPostItemClicked(post.id, post.userEmail ,
                holder,"ThumbsDown")
        }

        holder.deleteCardButton.setOnClickListener {
            onPostItemClickListener?.onPostItemClicked(post.id, post.userEmail ,
                holder,"DeleteCard")
        }

        holder.editCardButton.setOnClickListener {
            Log.v("APP", "edit clicked")
        }
    }

    private fun handleObjectPulling(holder: PostViewHolder, userEmail: String, post: Post) {
        holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_blank)
        holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_blank)
        when (userEmail) {
            in post.thumbsUpUsers -> {
                holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_filled)
            }
            in post.thumbsDownUsers -> {
                holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_filled)
            }
        }
        if (post.userEmail == userEmail) {
            holder.deleteCardButton.visibility = View.VISIBLE
            holder.editCardButton.visibility = View.VISIBLE
        }
        else {
            holder.deleteCardButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}