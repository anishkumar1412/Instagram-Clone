package com.example.instagramclone.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ReelDgBinding
import com.example.instagramclone.modals.Reel
import com.squareup.picasso.Picasso

class ReelAdapter(var context:Context, private var reelList:ArrayList<Reel>) :RecyclerView.Adapter<ReelAdapter.ViewHolder>(){
    inner class ViewHolder(var binding:ReelDgBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelAdapter.ViewHolder {
        val binding = ReelDgBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReelAdapter.ViewHolder, position: Int) {
        Picasso.get().load(reelList[position].profileLink).placeholder(R.drawable.user_icon).into(holder.binding.profileImageIcon)
        holder.binding.caption.text = reelList[position].captionReel
        holder.binding.videoView.setVideoPath(
            reelList[position].
        reelUrl)
        Log.d("ReelAdapter", "Loading video URL: ${reelList[position].reelUrl}")

        holder.binding.videoView.setOnPreparedListener {
            Log.d("ReelAdapter", "Video prepared for URL: ${reelList[position].reelUrl}")

            holder.binding.progressBar.visibility=View.GONE
            holder.binding.videoView.start()
            holder.binding.videoView
        }
        //Loop video feature
        holder.binding.videoView.setOnCompletionListener {
            holder.binding.videoView.start()
        }
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

}