package com.example.planttime.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.planttime.R
import com.example.planttime.databinding.ItemPlantBinding
import com.example.planttime.ui.viewmodel.PageViewModel
import java.util.*

@Suppress("DEPRECATION")
class PlantAdapter(private val viewModel: PageViewModel):  RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemPlantBinding ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val plantName = binding.plantName
        val plantIcon = binding.plantIcon
        val delButton = binding.delPlant
        val viewButton = binding.viewPlant
        val context = binding.root.getContext()
        init {
            // Define click listener for the ViewHolder's View.
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            println("Clicked on: $plantName")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = viewModel.plants.value?.get(position)
        holder.plantName.text = plant?.name

        //Display the plant icon, if registered. Otherwise, display the default cactus image.
        if (plant?.mediaRef.isNullOrEmpty()){
            holder.plantIcon.setImageResource(R.drawable.cactus)
        }
        else {
            Glide.with(holder.plantIcon)
                .load(plant?.mediaRef)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(holder.plantIcon)
        }

        //Button for deleting the selected plant:
        holder.delButton.setOnClickListener{
            val builder = AlertDialog.Builder(holder.context)
            builder.setMessage("Are you sure you want to delete \"${holder.plantName.text}\"?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    //Delete selected plant:
                    viewModel.deletePlant(position, holder.plantName.text.toString())
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        //Display the view button as clickable or not clickable:
        if (plant?.opening?.before(Date()) == true){ //Plant can be opened:
            val drawable = holder.context.resources.getDrawable(R.drawable.view_purple)
            holder.viewButton.foreground = drawable
            holder.viewButton.setOnClickListener {
                val viewPlantFragment = ViewPlantFragment.newInstance(plant, viewModel)
                val fragmentManager = (holder.context as AppCompatActivity).supportFragmentManager
                viewPlantFragment.show(fragmentManager, "ViewPlantFragment")
            }
        }
        else{ //Plant can't be opened yet:
            val drawable = holder.context.resources.getDrawable(R.drawable.view_grey)
            holder.viewButton.isClickable = false
            holder.viewButton.foreground = drawable
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        val plants = viewModel.plants.value
        return plants?.size ?: 1
    }
}

