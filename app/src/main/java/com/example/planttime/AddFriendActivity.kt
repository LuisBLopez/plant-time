package com.example.planttime

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.planttime.databinding.ActivityAddFriendBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding
    private val localUidSample = FirebaseAuth.getInstance().currentUser?.uid!!
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root)
    }

    private fun onViewCreated(view: View) {
        //Confirm adding new friend button:
        binding.addFriend.setOnClickListener{
            //Check that an email has been input:
            val email = binding.friendEmail.text
            if(!email.isNullOrEmpty()){
                //Retrieve the list of existing users from the database:
                db.collection("user").addSnapshotListener{
                    snapshot, e ->
                    if(e != null || snapshot == null){
                        Snackbar.make(view, "Sorry, your friend could not be added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        return@addSnapshotListener
                    }
                    else {
                        //Find the right user by checking their emails:
                        snapshot.documents.forEach{ field ->
                            val friendEmail = field.get("email")
                            if(friendEmail != null && friendEmail.toString() == email.toString()){
                                //The user with matching email has been found.
                                db.collection("user").document(localUidSample).get().addOnSuccessListener { user ->
                                    //If the local user has a friendlist already, add the friend directly:
                                    if (user.get("friends") != null) {
                                        db.collection("user").document(localUidSample)
                                            .update("friends", FieldValue.arrayUnion(field.id))
                                    }
                                    //Otherwise, create it and add the new friend:
                                    else {
                                        val data = hashMapOf("friends" to listOf(field.id))
                                        db.collection("user").document(localUidSample).set(data, SetOptions.merge())
                                    }
                                }
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
        //Return button:
        binding.back.setOnClickListener{
            finish()
        }
    }
}