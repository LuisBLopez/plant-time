package com.example.planttime

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.planttime.databinding.ActivityAddFriendBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding
    private val localUidSample = "l4VBLVnZeN1M7fMMhee8" //Placeholder user Id. This will later be modified whenever we implement the Log-in operations.
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root, savedInstanceState)
    }

    private fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addFriend.setOnClickListener{
            val email = binding.friendEmail.text
            if(!email.isNullOrEmpty()){
                db.collection("user").addSnapshotListener{
                    snapshot, e ->
                    if(e != null || snapshot == null){
                        Snackbar.make(view, "Sorry, your friend could not be added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        return@addSnapshotListener
                    }
                    else {
                        snapshot.documents.forEach{
                            val friendEmail = it.get("email")
                            if(friendEmail != null && friendEmail.toString() == email.toString()){
                                db.collection("user").document(localUidSample).update("friends", FieldValue.arrayUnion(it.id))
                                Snackbar.make(view, "Friend added!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                        }
                    }
                }
                finish()
            }
            else{
                Snackbar.make(view, "Please, enter the email of the friend you want to add", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        binding.back.setOnClickListener{
            finish()
        }
    }
}