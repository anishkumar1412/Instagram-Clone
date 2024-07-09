package com.example.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramclone.databinding.MyPostRvDesignBinding
import com.example.instagramclone.modals.Post
import com.example.instagramclone.modals.Reel
import com.squareup.picasso.Picasso


class MyReelAdapter(var context: Context, private var ReelList: ArrayList<Reel>):RecyclerView.Adapter<MyReelAdapter.ViewHolder>()  {
    inner class ViewHolder(var binding:MyPostRvDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ReelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    Glide.with(context).load(ReelList.get(position).reelUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(holder.binding.postImage)
    }
}