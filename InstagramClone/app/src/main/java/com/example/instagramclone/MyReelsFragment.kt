package com.example.instagramclone

import REEL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.adapters.MyPostAdapter
import com.example.instagramclone.adapters.MyReelAdapter
import com.example.instagramclone.databinding.FragmentMyReelsBinding
import com.example.instagramclone.modals.Post
import com.example.instagramclone.modals.Reel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject




class MyReelsFragment : Fragment() {
    private lateinit var binding:FragmentMyReelsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMyReelsBinding.inflate(inflater, container, false)
        var ReelList = ArrayList<Reel>()
        var adapter= MyReelAdapter(requireContext(),ReelList,)
        binding.myReelsRv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL,)
        binding.myReelsRv.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+REEL).get().addOnSuccessListener {
            var tempList = arrayListOf<Reel>()
            for(i in it.documents)
            {
                var reel: Reel = i.toObject<Reel>()!!
                tempList.add(reel)
            }
            ReelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}