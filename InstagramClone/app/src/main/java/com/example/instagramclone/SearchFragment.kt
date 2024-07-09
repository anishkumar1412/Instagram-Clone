package com.example.instagramclone

import USER_NODE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.adapters.SearchAdapter
import com.example.instagramclone.databinding.FragmentSearchBinding
import com.example.instagramclone.modals.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    lateinit var adapter:SearchAdapter
    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchRv.layoutManager= LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(),userList)
        binding.searchRv.adapter = adapter
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()

            for (i in it.documents)
            {
                if(i.id.toString().equals (Firebase.auth.currentUser!!.uid.toString()))
                {

                }else{
                    var user:User = i.toObject<User>()!!
                    tempList.add(user)
                }

            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        binding.searchbtn.setOnClickListener {
            var text = binding.searchView.text.toString()
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {
                var tempList = ArrayList<User>()
                userList.clear()
                if (it.isEmpty) {

                }
                else{

                for (i in it.documents) {
                    if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {

                    } else {
                        val user: User = i.toObject<User>()!!
                        tempList.add(user)
                    }

                }
                userList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
            }

        }
        return binding.root
    }

    companion object {

    }
}