package com.example.planttime.ui.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.planttime.R
import com.example.planttime.databinding.ItemPlantBinding
import com.example.planttime.ui.model.Plant
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.*

@Suppress("DEPRECATION")
class PlantAdapter(viewModel: PageViewModel):  RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val myPlants: List<Plant> = listOf(
            Plant(Date(),"RzZU71c31Zmi3vCiHbsC", false, "Sample cactus", Date()),
            Plant(Date(),"RzZU71c31Zmi3vCiHbsC", false, "Sample succulent", Date()),
            Plant(Date(),"RzZU71c31Zmi3vCiHbsC", false, "Sample dahlia", Date()))

    private val vModel = viewModel

    class ViewHolder(binding: ItemPlantBinding ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val plantName = binding.plantName
        val plantIcon = binding.plantIcon
        init {
            // Define click listener for the ViewHolder's View.
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            println("Clicked on: $plantName")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlantBinding.inflate(LayoutInflater.from(parent.context)))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.plantName.text = vModel.plants.value?.get(position)?.name ?: "Sample Rafflesia"
        holder.plantIcon.setImageResource(R.drawable.cactus) //Placeholder image for now
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        return vModel.plants.value?.size ?: 3
    }
}