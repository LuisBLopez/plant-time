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
            setIndex(1)
        }
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

        binding.fab.setOnClickListener {
            Snackbar.make(view, "Create a new plant", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            val intent = Intent(requireActivity(), AddPlantActivity::class.java)
            startActivity(intent)
        }
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)

    }*/

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