package com.example.instagramclone.adapters

import USER_NODE
import POST
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.PostRvBinding
import com.example.instagramclone.modals.Post
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("seen_posts", Context.MODE_PRIVATE)

    inner class MyHolder(var binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val post = postList[position]

        // Mark the post as seen
        markPostAsSeen(post.id)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: return

        try {
            Firebase.firestore.collection(USER_NODE).document(post.uid).get()
                .addOnSuccessListener {
                    val user = it.toObject<com.example.instagramclone.modals.User>()
                    Glide.with(context).load(user!!.image).placeholder(R.drawable.user_icon).into(holder.binding.profileImage)
                    holder.binding.name.text = user.name
                }
        } catch (e: Exception) {
            // Handle exception
        }

        Glide.with(context).load(post.postUrl).placeholder(R.drawable.loading).into(holder.binding.postImage)
        try {
            val text = TimeAgo.using(post.time.toLong())
            holder.binding.time.text = text
        } catch (e: Exception) {
            // Handle exception
        }

        holder.binding.share.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, post.postUrl)
            context.startActivity(i)
        }

        holder.binding.caption.text = post.caption

        // Determine if the current user has liked this post
        val userHasLiked = post.likedBy.contains(currentUserId)
        updateLikeButton(holder, userHasLiked)

        holder.binding.like.setOnClickListener {
            if (userHasLiked) {
                // User has already liked the post, so we unlike it
                post.likes -= 1
                post.likedBy.remove(currentUserId)
                updateLikeCount(post.id, post.likes)
                updateLikedByList(post.id, currentUserId, false)
                updateLikeButton(holder, false)
            } else {
                // User has not liked the post yet, so we like it
                post.likes += 1
                post.likedBy.add(currentUserId)
                updateLikeCount(post.id, post.likes)
                updateLikedByList(post.id, currentUserId, true)
                updateLikeButton(holder, true)
            }
            holder.binding.likecount.text = "${post.likes} likes"
        }

        holder.binding.likecount.text = "${post.likes} likes"
    }

    private fun updateLikeButton(holder: MyHolder, liked: Boolean) {
        if (liked) {
            holder.binding.like.setImageResource(R.drawable.heart1)
        } else {
            holder.binding.like.setImageResource(R.drawable.heart)
        }
    }

    private fun markPostAsSeen(postId: String) {
        val editor = sharedPreferences.edit()
        val seenPosts = sharedPreferences.getStringSet("seen_posts", mutableSetOf()) ?: mutableSetOf()
        seenPosts.add(postId)
        editor.putStringSet("seen_posts", seenPosts)
        editor.apply()
    }

    private fun updateLikeCount(postId: String, newLikeCount: Int) {
        Firebase.firestore.collection(POST).document(postId)
            .update("likes", newLikeCount)
            .addOnSuccessListener {
                // Like count updated successfully
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun updateLikedByList(postId: String, userId: String, like: Boolean) {
        val postRef = Firebase.firestore.collection(POST).document(postId)
        if (like) {
            postRef.update("likedBy", FieldValue.arrayUnion(userId))
        } else {
            postRef.update("likedBy", FieldValue.arrayRemove(userId))
        }
    }
}
