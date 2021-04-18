package com.example.planttime.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.planttime.R
import com.example.planttime.databinding.FragmentViewPlantBinding
import com.example.planttime.ui.model.Friend
import com.example.planttime.ui.model.Plant
import com.example.planttime.ui.viewmodel.PageViewModel

class ViewPlantFragment : DialogFragment() {

    private lateinit var binding: FragmentViewPlantBinding
    private lateinit var plantName: String
    private lateinit var expirationDate: String
    private lateinit var creator: String
    private lateinit var letter: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = FragmentViewPlantBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_plant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user.observe(this,{binding.plantCreator.text = it.toString()}) //wait until the value for the creator has been retrieved
        binding.plantName.text = arguments?.getString(NAME).toString()
        binding.expirationDate.text = arguments?.getString(EXPDATE).toString()
        binding.plantLetter.text = arguments?.getString(LETTER).toString()
    }

    companion object {
        private const val NAME = "name"
        private const val EXPDATE = "expirationDate"
        private const val LETTER = "letter"
        private var user: MutableLiveData<Friend> = MutableLiveData<Friend>() //User for the creator of the plant
        fun newInstance(plant: Plant, pageViewModel: PageViewModel): ViewPlantFragment {
            pageViewModel.getUser(plant.creator) //obtain creator as user from database
            user = pageViewModel.user //retrieve user from pageviewmodel
            val arguments = Bundle()
            arguments.putString(NAME, plant.name)
            arguments.putString(EXPDATE, "${plant.opening?.date}/${plant.opening?.month?.plus(1)}/${plant.opening?.year?.plus(1900)}")
            arguments.putString(LETTER, plant.letter)
            val fragment = ViewPlantFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}