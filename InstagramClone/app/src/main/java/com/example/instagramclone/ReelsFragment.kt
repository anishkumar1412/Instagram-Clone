package com.example.instagramclone

import REEL
import REEL_FOLDER
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramclone.adapters.ReelAdapter
import com.example.instagramclone.databinding.FragmentReelsBinding
import com.example.instagramclone.modals.Reel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class ReelsFragment : Fragment() {
    private lateinit var binding: FragmentReelsBinding
    private lateinit var adapter: ReelAdapter
    private var reelList = ArrayList<Reel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReelsBinding.inflate(inflater, container, false)
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewpager.adapter = adapter
        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            var tempList = ArrayList<Reel>()
            reelList.clear()
            for (i in it.documents) {
                var reel = i.toObject<Reel>()!!


                tempList.add(reel)
                Log.d("ReelsFragment", "Reel fetched :$reel")
            }
            reelList.addAll(tempList)
            reelList.reverse()
            adapter.notifyDataSetChanged()
        }.addOnFailureListener{e->
            Log.e("ReelsFragment","Error fetching data",e)
        }

        return binding.root
    }

    companion object {
        const val REEL = "Reel"
    }
}