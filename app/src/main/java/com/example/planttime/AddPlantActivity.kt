package com.example.planttime

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planttime.databinding.ActivityAddPlantBinding
import com.example.planttime.ui.view.DatePickerFragment
import com.google.android.material.snackbar.Snackbar

class AddPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_plant)

        onViewCreated(binding.root, savedInstanceState)
    }

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAddPlantBinding.inflate(inflater, container, false)
        return binding.root
    }*/

    fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.plantExpiration.setOnClickListener {
            Snackbar.make(view, "Choose a date when the plant will bloom", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val datePickerFrag = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener{ _, year, month, day ->
                // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
                binding.plantExpiration.setText(selectedDate)
            })
            datePickerFrag.show(supportFragmentManager, "datePicker")
        }

        binding.plantConfirmAdd.setOnClickListener {
            Snackbar.make(view, "Creating a new plant...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}