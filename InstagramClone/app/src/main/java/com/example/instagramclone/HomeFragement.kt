package com.example.instagramclone

import FOLLOW
import POST
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.adapters.FollowAdapter
import com.example.instagramclone.adapters.PostAdapter
import com.example.instagramclone.databinding.FragmentHomeFragementBinding
import com.example.instagramclone.modals.Post
import com.example.instagramclone.modals.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class HomeFragement : Fragment() {
    private lateinit var binding: FragmentHomeFragementBinding
    private var postList = ArrayList<Post>()
    private lateinit var adapter: PostAdapter
    private lateinit var followAdapter: FollowAdapter
    private var followList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeFragementBinding.inflate(inflater, container, false)

        // Set up the PostAdapter
        adapter = PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postRv.adapter = adapter

        // Set up the FollowAdapter
        followAdapter = FollowAdapter(requireContext(), followList)
        binding.followRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.followRv.adapter = followAdapter

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        // Fetch follow list
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get().addOnSuccessListener {
            val tempList = ArrayList<User>()
            followList.clear()
            for (i in it.documents) {
                val user: User? = i.toObject<User>()
                if (user != null) {
                    tempList.add(user)
                }
            }
            followList.addAll(tempList)
            followAdapter.notifyDataSetChanged()
        }

        // Fetch post list
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            val tempList = ArrayList<Post>()
            postList.clear()
            for (document in it.documents) {
                val post: Post? = document.toObject<Post>()?.apply { id = document.id }
                if (post != null) {
                    tempList.add(post)
                }
            }
            postList.addAll(tempList)
            sortPosts()
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.opt_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun sortPosts() {
        val sharedPreferences = requireContext().getSharedPreferences("seen_posts", Context.MODE_PRIVATE)
        val seenPosts = sharedPreferences.getStringSet("seen_posts", mutableSetOf()) ?: mutableSetOf()

        // Sort posts with unseen posts first
        postList.sortWith(compareBy { it.id !in seenPosts })
        Log.d("HomeFragment", "Sorted posts: ${postList.map { it.id to (it.id !in seenPosts) }}")
    }
}
