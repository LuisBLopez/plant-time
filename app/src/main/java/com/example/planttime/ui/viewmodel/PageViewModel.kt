package com.example.planttime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.planttime.ui.model.Plant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    private lateinit var db: FirebaseFirestore
    private val localUidSample = "RzZU71c31Zmi3vCiHbsC"
    private var _plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()

    init {
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToUserPlants()
    }

    private fun listenToUserPlants() {
        db.collection("user").document(localUidSample).collection("plants").addSnapshotListener {
            snapshot, e ->
            if (e != null){
                println("ERROR: Could not listen to user plant list. "+e)
                return@addSnapshotListener
            }
            else if (snapshot != null) {
                val allPlants = ArrayList<Plant>()
                val documents = snapshot.documents
                documents.forEach {
                    val plant = it.toObject(Plant::class.java)
                    if (plant != null){
                        allPlants.add(plant)
                    }
                }
                _plants.value = allPlants
            }
        }
    }

    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    internal var plants:MutableLiveData<ArrayList<Plant>>
        get() {return _plants}
        set(value) {_plants = value}
}