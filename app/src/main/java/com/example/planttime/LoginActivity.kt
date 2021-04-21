package com.example.planttime

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planttime.databinding.ActivityLoginBinding
import com.example.planttime.ui.model.Friend
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnRegister.setOnClickListener {
            if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email,password).addOnCompleteListener {
                                    val data = Friend("Newbie Gardener", email)
                                    val newUid = FirebaseAuth.getInstance().currentUser?.uid
                                    if (!newUid.isNullOrEmpty())
                                        db.collection("user").document(newUid).set(data)

                                    if (it.isSuccessful) {
                                        Snackbar.make(view, "Registered successfully!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show()

                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Snackbar.make(view, "Registered, but could not log in. Please try again", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show()
                                    }
                                }
                        } else {
                            Snackbar.make(view, "Could not register.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }
                }
            }
            else {
                Snackbar.make(view, "Please fill up the username and password fields first.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Snackbar.make(view, "Logged in successfully!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Snackbar.make(view, "Could not log in. Inputs may be wrong or user may not be registered.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }
                    }
            }
            else {
                Snackbar.make(view, "Please fill up the username and password fields first.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }
}