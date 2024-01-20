package com.app.wisebuyer.posts

import com.app.wisebuyer.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostCardsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostCardsAdapter.PostViewHolder>() {
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.card_image)
        val title: TextView = itemView.findViewById(R.id.card_title)
        val description: TextView = itemView.findViewById(R.id.card_description)
        val link: TextView = itemView.findViewById(R.id.card_link)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        // TODO: Add more logic here: thumbs up / down for post etc
        val post = posts[position]
        holder.title.text = post.title
        holder.description.text = post.description
        holder.link.text = post.link
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
