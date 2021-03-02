package com.example.planttime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.planttime.ui.model.Plant
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class PageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val localUidSample = "RzZU71c31Zmi3vCiHbsC"
    private lateinit var plantList: List<Plant>

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun loadInitialData() {
        db.collection("user").document(localUidSample).get().addOnSuccessListener {
            plantList = it.get(FieldPath.of("plants")) as List<Plant>
        }
    }

    fun getCurrentPlants(): List<Plant> {
        return plantList
    }
}