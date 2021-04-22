package com.example.planttime.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planttime.AddPlantActivity
import com.example.planttime.databinding.FragmentMyPlantsBinding
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.android.material.snackbar.Snackbar

class MyPlantsFragment: Fragment() {
    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentMyPlantsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(1) //First tab of the main screen.
        }

        //Observe changes in the local user's plants list in real time:
        pageViewModel.plants.observe(this, { plants ->
            binding.recyclerView.adapter = PlantAdapter(pageViewModel)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = PlantAdapter(pageViewModel)

        //Button for adding a new plant:
        binding.fab.setOnClickListener {
            Snackbar.make(view, "Create a new plant", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            //Move to the adding plant procedure activity:
            val intent = Intent(requireActivity(), AddPlantActivity::class.java)
            startActivity(intent)
        }
    }
}