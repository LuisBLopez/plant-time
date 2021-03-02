package com.example.planttime.ui.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.planttime.databinding.ItemPlantBinding
import com.example.planttime.ui.model.Plant
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

@Suppress("DEPRECATION")
class PlantAdapter(plantList: List<Plant>):  RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val myPlants: List<Plant> = listOf(
            Plant(LocalDate.now(), "RzZU71c31Zmi3vCiHbsC", false, "Cactus", LocalDate.now()),
            Plant(LocalDate.now(), "l4VBLVnZeN1M7fMMhee8", true, "Succulent", LocalDate.now()),
            Plant(LocalDate.now(), "l4VBLVnZeN1M7fMMhee8", false, "Dahlia", LocalDate.now()))

    //private val db = FirebaseFirestore.getInstance()
    //private val localUidSample = "RzZU71c31Zmi3vCiHbsC"
    private lateinit var plantList: List<Plant>

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
        println("Reading plant names AAAAA")
        val p = plantList[position]

        holder.plantName.text = p.name

        /*db.collection("user").document(localUidSample).get().addOnSuccessListener {
            val plantName = it.get(FieldPath.of("plants","plant${position}","name")) as String?
            holder.plantName.text = plantName
        }.addOnFailureListener { //Placeholder plants
            var plant = myPlants[position]
            holder.plantName.text = "${plant.name} created on ${plant.time_created.toString()}"
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        return plantList.size
        /*var plantsCount = 0

        db.collection("user").document(localUidSample).get().addOnSuccessListener {
            var stop = false
            var plant = 1
            var plantsCount = 0
            while (!stop) {
                if (it.get(FieldPath.of("plants", "plant${plant}", "name")) != null) {
                    println("Plants count:${plantsCount}, plant:${plant}")
                    plant++
                    plantsCount++
                }
                else stop = true
            }
        }.addOnFailureListener{
            plantsCount = myPlants.size
        }
        println("Number of plants:${plantsCount}")
        return plantsCount
        */
        //return myPlants.size //Placeholder value
    }

}

