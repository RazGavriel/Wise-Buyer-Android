package com.app.wisebuyer.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.wisebuyer.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class PostCardsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostCardsAdapter.PostViewHolder>() {
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email as String

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.card_image)
        val title: TextView = itemView.findViewById(R.id.card_title)
        val description: TextView = itemView.findViewById(R.id.card_description)
        val link: TextView = itemView.findViewById(R.id.card_link)
        val price: TextView = itemView.findViewById(R.id.card_price)
        val imageThumbsUp: ImageView = itemView.findViewById(R.id.image_thumbs_up)
        val imageThumbsDown: ImageView = itemView.findViewById(R.id.image_thumbs_down)
        val deleteCardButton: Button = itemView.findViewById(R.id.delete_card_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card_item, parent, false)
        return PostViewHolder(view)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
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
        handleClicksCard(holder, position)
        handleThumbsOnStart(holder, position)
    }

    private fun handleThumbsOnStart(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        fillThumbsUI(userEmail, post.thumbsUpUsers, post.thumbsDownUsers, holder)
   }

    private suspend fun cardDBHandler(postId: String, email: String,
                                      holder: PostViewHolder, mode: String) {
        if (mode == "ThumbsUp" || mode == "ThumbsDown") {
            updateThumbsData(postId, email, mode, holder)
        }
    }

    private fun handleClicksCard(holder: PostViewHolder, position: Int) {
        holder.imageThumbsUp.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                cardDBHandler(posts[position].id, userEmail, holder,"ThumbsUp")
            }
        }
        holder.imageThumbsDown.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                cardDBHandler(posts[position].id, userEmail, holder, "ThumbsDown")
            }
        }

        holder.deleteCardButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                cardDBHandler(posts[position].id, userEmail, holder, "DeleteCard")
            }
        }
    }
    private suspend fun updateThumbsData(postId: String, email: String, mode: String,
                                         holder: PostViewHolder)
    {
        try {
            val documentReference = db.collection("Posts").document(postId)
            val documentSnapshot = documentReference.get().await()
            if (documentSnapshot.exists()) {
                val thumbsUpUsers = (documentSnapshot.get("thumbsUpUsers")
                    as? List<String>)?.toMutableList() ?: mutableListOf()
                val thumbsDownUsers = (documentSnapshot.get("thumbsDownUsers")
                    as? List<String>)?.toMutableList() ?: mutableListOf()
                val (finalThumbsUpUsers, finalThumbsDownUsers) =
                    thumbsArrayHandler(mode, email, thumbsUpUsers, thumbsDownUsers)

                documentReference.update("thumbsUpUsers", finalThumbsUpUsers,
                    "thumbsDownUsers", finalThumbsDownUsers).await()
                fillThumbsUI(email, finalThumbsUpUsers, finalThumbsDownUsers, holder)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToast("It seems like there was an issue updating thumbs. " +
                        "Please try again later.")
            }
        }
    }
    private fun thumbsArrayHandler(mode: String, email: String, thumbsUpUsers: MutableList<String>,
                                   thumbsDownUsers: MutableList<String>
    ): Pair<MutableList<String>, MutableList<String>> {
        when (mode) {
            "ThumbsUp" -> {
                when (email) {
                    in thumbsUpUsers -> { thumbsUpUsers.remove(email) }
                    in thumbsDownUsers -> {
                        thumbsDownUsers.remove(email)
                        thumbsUpUsers.add(email)
                    }
                    else -> { thumbsUpUsers.add(email) }
                }
            }
            "ThumbsDown" -> {
                when (email) {
                    in thumbsDownUsers -> { thumbsDownUsers.remove(email) }
                    in thumbsUpUsers -> {
                        thumbsUpUsers.remove(email)
                        thumbsDownUsers.add(email)
                    }
                    else -> { thumbsDownUsers.add(email) }
                }
            }
        }
        return Pair(thumbsUpUsers, thumbsDownUsers)
    }
    private fun fillThumbsUI(email: String, finalThumbsUpUsers: List<String>,
                             finalThumbsDownUsers : List<String>, holder: PostViewHolder){
        holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_blank)
        holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_blank)
        when (email) {
            in finalThumbsUpUsers -> {
                holder.imageThumbsUp.setImageResource(R.drawable.thumb_up_filled)
            }
            in finalThumbsDownUsers -> {
                holder.imageThumbsDown.setImageResource(R.drawable.thumb_down_filled)
            }
        }
    }
    private suspend fun showToast(message: String) { withContext(Dispatchers.Main) {} }

    override fun getItemCount(): Int {
        return posts.size
    }
}