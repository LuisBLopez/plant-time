package com.example.planttime.ui.api

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.planttime.R
import com.example.planttime.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

        viewModel.photos.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        val editor = sharedPref.edit()
        editor.putString("chosen_pic_url", photo.urls.thumb)
        editor.apply()
        binding.apply{
            Glide.with(this.plantPicChosen)
                    .load(photo.urls.thumb)
                    .centerCrop()
                    .into(this.plantPicChosen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}