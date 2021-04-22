package com.example.planttime.ui.api

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
): ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap{ queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    companion object {
        private const val DEFAULT_QUERY = "plants"
    }
}