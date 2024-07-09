package com.example.instagramclone

import USER_NODE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.adapters.ViewPagerAdapter
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize ViewPagerAdapter
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager) // Use childFragmentManager

        // Add fragments to the adapter
        viewPagerAdapter.addFragments(MyPostFragment(), "My Posts")
        viewPagerAdapter.addFragments(MyReelsFragment(), "My Reels")

        // Set the adapter to the ViewPager
        binding.viewpager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)

        // Set the Edit Profile button click listener
        binding.editProfileBtn.setOnClickListener {
            val intent = Intent(activity, Signup::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection(USER_NODE).document(userId).get()
                .addOnSuccessListener {
                    val user = it.toObject<User>()
                    if (user != null) {
                        binding.name.text = user.name
                        binding.bio.text = user.email
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImageIcon)
                        }
                    }
                }
        }
    }
}
