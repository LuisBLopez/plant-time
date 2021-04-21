package com.example.planttime.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.planttime.LoginActivity
import com.example.planttime.MainActivity
import com.example.planttime.databinding.FragmentSettingsBinding
import com.example.planttime.ui.viewmodel.PageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

/**
 * A placeholder fragment containing a simple view.
 */
class SettingsFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var binding: FragmentSettingsBinding
    private var notifications: String = "On"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(3)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageViewModel.self.observe(viewLifecycleOwner,{ self ->
            binding.name.setText(self.nickname)
            binding.email.text = self.email
        })
        binding.notifications.setOnClickListener(){
            notifications = if(notifications.equals("On")) "Off"
            else "On"
            binding.notifications.text= "Notifications: $notifications"
        }
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
        binding.logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            /*val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)*/
            requireActivity().finish()
        }
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

