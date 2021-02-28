package com.example.planttime.ui.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.planttime.databinding.ItemPlantBinding
import com.example.planttime.ui.model.Plant
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

@Suppress("DEPRECATION")
class PlantAdapter():  RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val myPlants: List<Plant> = listOf(Plant(0, "Cactus", LocalDate.now()), Plant(1, "Succulent", LocalDate.now()), Plant(2, "Dahlia", LocalDate.now()))

    private val db = FirebaseFirestore.getInstance()
    private val localUidSample = "RzZU71c31Zmi3vCiHbsC"

    class ViewHolder(binding: ItemPlantBinding ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val plantName = binding.plantName
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
        db.collection("user").document(localUidSample).get().addOnSuccessListener {
            val plantName = it.get("plants/plant${position}/name") as String?
            holder.plantName.text = plantName
        }

        //var plant = myPlants[position]
        //holder.plantName.text = "${plant.name} created on ${plant.time_created.toString()}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount() = myPlants.size

}