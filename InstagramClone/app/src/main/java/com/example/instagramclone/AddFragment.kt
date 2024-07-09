package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import posts.ReelsActivity
import posts.postActivity

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.addPost.setOnClickListener {
            startActivity(Intent(requireContext(), postActivity::class.java))
            activity?.finish()
        }
        binding.addReels.setOnClickListener {
            startActivity(Intent(requireContext(), ReelsActivity::class.java))
            activity?.finish()
        }
        return binding.root
    }

    companion object {
    }
}
