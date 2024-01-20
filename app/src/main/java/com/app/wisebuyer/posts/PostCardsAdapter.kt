package com.app.wisebuyer.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.text.NumberFormat
import java.util.Locale


class PostCardsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostCardsAdapter.PostViewHolder>() {

    private val storage = FirebaseStorage.getInstance()
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.card_image)
        val title: TextView = itemView.findViewById(R.id.card_title)
        val description: TextView = itemView.findViewById(R.id.card_description)
        val link: TextView = itemView.findViewById(R.id.card_link)
        val price: TextView = itemView.findViewById(R.id.card_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        // TODO: Add more logic here: thumbs up / down for post etc
        val post = posts[position]
        storage.reference.child(post.productPicture).downloadUrl.addOnSuccessListener {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.image);
        }
        holder.price.text = NumberFormat.getCurrencyInstance(Locale.US).format(post.price.toInt())
        "${post.title} | ${post.productType}".also { holder.title.text = it }
        holder.description.text = post.description
        holder.link.text = post.link
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
