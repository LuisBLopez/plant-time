package com.example.planttime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.planttime.ui.model.Friend
import com.example.planttime.ui.model.Plant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val localUidSample = FirebaseAuth.getInstance().currentUser?.uid!!
    private var _plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()
    private var _friends: MutableLiveData<ArrayList<Friend>> = MutableLiveData<ArrayList<Friend>>()
    private var _self: MutableLiveData<Friend> = MutableLiveData<Friend>() //Self
    private var _user: MutableLiveData<Friend> = MutableLiveData<Friend>() //User to be retrieved

    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToUserPlants()
        listenToFriends()
        getSelf()
    }

    private fun listenToUserPlants() {
        db.collection("user").document(localUidSample).collection("plants")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("ERROR: Could not listen to user plant list. " + e)
                    return@addSnapshotListener
                } else if (snapshot != null) {
                    val allPlants = ArrayList<Plant>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val plant = it.toObject(Plant::class.java)
                        if (plant != null) {
                            allPlants.add(plant)
                        }
                    }
                    _plants.value = allPlants
                }
            }
    }

    private fun listenToFriends() {
        db.collection("user").addSnapshotListener {
            snapshot, e ->
            if (e != null){
                println("ERROR: Could not listen to user friend list. "+e)
                return@addSnapshotListener
            }
            else if (snapshot != null) {

                var allFriendsUids: ArrayList<String>
                val allFriends = ArrayList<Friend>()

                snapshot.documents.forEach { d ->
                    if (d.id.equals(localUidSample)){
                        if (d.get("friends") != null) {
                            //Extract friends list
                            allFriendsUids = d.get("friends") as ArrayList<String>

                            allFriendsUids.forEach{ f ->
                                snapshot.documents.forEach {
                                    if (it.id.equals(f)){
                                        //Create a Friend object and add it to the list
                                        val nick = it.get("nickname") as String?
                                        val email = it.get("email") as String?
                                        if (nick != null && email != null) {
                                            allFriends.add(Friend(nick, email))
                                        }
                                    }
                                }
                            }
                            _friends.value = allFriends
                        }
                    }
                }
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
    internal var friends:MutableLiveData<ArrayList<Friend>>
        get() {return _friends}
        set(value) {_friends = value}
    internal var self:MutableLiveData<Friend>
        get() {return _self}
        set(value) {_self = value}
    internal var user:MutableLiveData<Friend>
        get() {return _user}
        set(value) {_user = value}

    fun deletePlant(position: Int, plantName: String) {
        //Find the plant in the database and delete it:
        db.collection("user").document(localUidSample).collection("plants").get().addOnSuccessListener {
            val plant = it.documents.get(position)
            if (plant.getString("name").equals(plantName))  {
                db.collection("user").document(localUidSample).collection("plants").document(plant.id).delete()
            }
        }
    }

    fun changeNickname(nickname: String){
        db.collection("user").document(localUidSample).update("nickname", nickname)
        _self.value?.nickname = nickname
        self.value?.nickname = nickname
    }

    private fun getSelf(){
        db.collection("user").document(localUidSample).get().addOnSuccessListener {
            val name = it.get("nickname").toString()
            val email = it.get("email").toString()
            if(email.isNullOrEmpty() || name.isNullOrEmpty()){
                println("Could not get self")
                return@addOnSuccessListener
            }
            else{
                val self = Friend(name, email)
                _self.value = self
            }
        }
    }
    fun getUser(Id: String) {
        db.collection("user").document(Id).get().addOnSuccessListener {
            val name = it.get("nickname").toString()
            val email = it.get("email").toString()
            if(email.isNullOrEmpty() || name.isNullOrEmpty()){
                println("Could not get friend")
                return@addOnSuccessListener
            }
            else{
                _user.value = Friend(name, email)
            }
        }
    }
}