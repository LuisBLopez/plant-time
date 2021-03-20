package com.example.planttime.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.planttime.ui.model.Plant
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class PageViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val localUidSample = "RzZU71c31Zmi3vCiHbsC"

    private lateinit var plantList: List<Plant>
    private var _plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadInitialData() {
        /*db.collection("user").document(localUidSample).get().addOnSuccessListener {
            plantList = it.get(FieldPath.of("plants")) as List<Plant>
        }*/

        /**Alternative version wip using MutableLiveData**/
        db.collection("user").document(localUidSample).collection("plantas").addSnapshotListener {
            snapshot, e ->
            if (e != null) {
               // Log.w(TAG, "Listen failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val allPlants = ArrayList<Plant>()
                val documents = snapshot.documents
                documents.forEach {
                    val plant = it.toObject(Plant::class.java)
                    if (plant != null){
                        allPlants.add(plant!!)
                    }
                }
                _plants.value = allPlants
            }
        }

    }

    fun getCurrentPlants(): List<Plant> {
        return plantList
    }

    /**Alternative version wip using MutableLiveData**/
    internal var plants:MutableLiveData<ArrayList<Plant>>
        get(){return _plants}
        set(value){_plants = value}
}