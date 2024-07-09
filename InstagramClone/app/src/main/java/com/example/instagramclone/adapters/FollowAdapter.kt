package com.example.instagramclone.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.databinding.FollowRvBinding
import com.example.instagramclone.modals.User

class FollowAdapter(var context: Context, private var followList: ArrayList<User>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FollowRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FollowRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = followList[position]

        // Logging to verify user data
        Log.d("FollowAdapter", "Binding user: ${user.name}, Image: ${user.image}")

        Glide.with(context)
            .load(user.image)
            .placeholder(R.drawable.user_icon)
            .into(holder.binding.profileImage)
        holder.binding.name.text = user.name

        // Logging to verify TextView update
        Log.d("FollowAdapter", "TextView name updated: ${holder.binding.name.text}")
    }
}
