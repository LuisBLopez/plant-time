package com.example.planttime.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planttime.databinding.FragmentSettingsBinding
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentSettingsBinding
    private var notifications: String = "On"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(3) //Third tab of the main screen.
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observe updates of the self variable to display them on screen:
        pageViewModel.self.observe(viewLifecycleOwner,{ self ->
            binding.name.setText(self.nickname)
            binding.email.text = self.email
        })

        //Trigger notifications on or off:
        binding.notifications.setOnClickListener(){
            notifications = if(notifications.equals("On")) "Off"
            else "On"
            binding.notifications.text= "Notifications: $notifications"
        }

        //Update the nickname in the database (and "self" variable) when done editing it:
        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }
            override fun afterTextChanged(s: Editable?) {
                pageViewModel.changeNickname(s.toString())
            }
        })

        //Log out from Firebase Auth and return to the log in activity:
        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
        }
        binding.name.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }
            override fun afterTextChanged(s: Editable?) {
                pageViewModel.changeNickname(s.toString())
            }
        })
    }

    companion object {
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
        fun newInstance(sectionNumber: Int): SettingsFragment {
            return SettingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}

