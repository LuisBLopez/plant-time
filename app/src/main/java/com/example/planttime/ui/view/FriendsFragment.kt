package com.example.planttime.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planttime.AddFriendActivity
import com.example.planttime.databinding.FragmentFriendsBinding
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.android.material.snackbar.Snackbar

class FriendsFragment: Fragment() {
    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(2)
        }
        pageViewModel.friends.observe(this, { friends ->
            binding.recyclerView.adapter = FriendAdapter(pageViewModel)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = FriendAdapter(pageViewModel)
        binding.fab.setOnClickListener {
            Snackbar.make(view, "Add a new friend", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val intent = Intent(requireActivity(), AddFriendActivity::class.java)
            //intent.putExtra("firestore",pageViewModel)
            startActivity(intent)
        }
    }
}