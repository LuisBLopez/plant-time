package com.example.planttime.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planttime.databinding.ItemFriendBinding
import com.example.planttime.ui.viewmodel.PageViewModel

class FriendAdapter(viewModel: PageViewModel): RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    private val vModel = viewModel

    class ViewHolder(binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val friend = binding.friendEmail
        init {
            // Define click listener for the ViewHolder's View.
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            println("Clicked on: $friend")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.friend.text = vModel.friends.value?.get(position)?.toString() ?: "You have no friends registered yet. Add one using the button below! :)"
    }

    override fun getItemCount(): Int {
        return vModel.friends.value?.size ?: 1
    }
}