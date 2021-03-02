package com.example.planttime.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planttime.databinding.FragmentMyPlantsBinding
import com.example.planttime.ui.model.Plant
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.android.material.snackbar.Snackbar

class MyPlantsFragment: Fragment() {
    private lateinit var pageViewModel: PageViewModel
    //private val myPlants: List<Plant> = listOf(Plant(0, "Cactus", Calendar.getInstance().time), Plant(1, "Succulent", Calendar.getInstance().time), Plant(2, "Dahlia", Calendar.getInstance().time))
    private lateinit var binding: FragmentMyPlantsBinding

    private lateinit var plantList: List<Plant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(1)
            loadInitialData()
            plantList = getCurrentPlants()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = PlantAdapter(plantList)

        binding.fab.setOnClickListener {
            Snackbar.make(view, "Create a new plant", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

        }


    }
    /*companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }*/
}