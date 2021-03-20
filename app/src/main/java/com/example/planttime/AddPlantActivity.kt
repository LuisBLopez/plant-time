package com.example.planttime

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.planttime.databinding.ActivityAddPlantBinding
import com.example.planttime.ui.model.Plant
import com.example.planttime.ui.view.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.util.*
import kotlin.collections.HashMap

class AddPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantBinding
    private val localUidSample = "l4VBLVnZeN1M7fMMhee8" //Placeholder user Id. This will later be modified whenever we implement the Log-in operations.
    private lateinit var db: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)//(R.layout.activity_add_plant)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var set_day = 0
        var set_month = 0
        var set_year = -1

        binding.plantExpiration.setOnClickListener {
            Snackbar.make(view, "Choose a date when the plant will bloom", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val datePickerFrag = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener{ _, year, month, day ->
                // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year

                set_day = day
                set_month = month
                set_year = year

                binding.plantExpiration.setText(selectedDate)
            })
            datePickerFrag.show(supportFragmentManager, "datePicker")
        }

        binding.plantConfirmAdd.setOnClickListener {
            if (!binding.plantName.text.isNullOrEmpty() && !binding.plantExpiration.text.isNullOrEmpty()) {
                Snackbar.make(view, "Creating a new plant...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                var plantId = "30" //Compute new available plant id here

                println("STARTING SETTING STUFF MAYBE")

                val data = Plant(Date(),localUidSample,false,binding.plantName.text.toString(),Date(set_year,set_month,set_day))
                db.collection("user").document(localUidSample).collection("plants").document().set(data)

                println("DONE SETTING STUFF MAYBE")
            }
            else {
                Snackbar.make(view, "Please, fill up at least the name and expiration date.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }
}