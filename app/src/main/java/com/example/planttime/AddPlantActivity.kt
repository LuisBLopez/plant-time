package com.example.planttime

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planttime.databinding.ActivityAddPlantBinding
import com.example.planttime.ui.app.PlantTimeApp
import com.example.planttime.ui.model.Plant
import com.example.planttime.ui.security.AES
import com.example.planttime.ui.view.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantBinding
    private val localUidSample = FirebaseAuth.getInstance().currentUser?.uid!!
    private lateinit var db: FirebaseFirestore
    private var aes = AES()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onViewCreated(view: View) {
        var setDay = 0
        var setMonth = 0
        var setYear = -1

        //Trigger the datepicker screen to set an expiration date:
        binding.plantExpiration.setOnClickListener {
            Snackbar.make(view, "Choose a date when the plant will bloom", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val datePickerFrag = DatePickerFragment.newInstance { _, year, month, day ->
                // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year

                setDay = day
                setMonth = month
                setYear = year

                binding.plantExpiration.setText(selectedDate)
            }
            datePickerFrag.show(supportFragmentManager, "datePicker")
        }

        //Button for finishing the add plant process:
        binding.plantConfirmAdd.setOnClickListener {
            val letter = binding.plantLetter.text
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val strPicUrl = sharedPref.getString("chosen_pic_url", "No_photo_chosen_yet")

            //Check that the mandatory fields are filled:
            if (!binding.plantName.text.isNullOrEmpty() && !binding.plantExpiration.text.isNullOrEmpty() && !letter.isNullOrEmpty()) {
                Snackbar.make(view, "Creating a new plant...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                //Create a new plant with the gathered data:
                val data = Plant(
                    Date(), localUidSample, false, binding.plantName.text.toString(), Date(
                        setYear - 1900,
                        setMonth,
                        setDay
                    )
                )

                //If a picture has been chosen, attach its url. Otherwise, it will use a default preset cactus picture.
                if (strPicUrl != "No_photo_chosen_yet") data.mediaRef = strPicUrl

                //Clean the shared preferences for the next plant.
                sharedPref.edit().putString("chosen_pic_url", "No_photo_chosen_yet").apply()

                //Encrypt the letter with AES:
                val context = PlantTimeApp.applicationContext()
                val cipherText = aes.encrypt(context, letter.toString())
                val sb = StringBuilder()
                for(c in cipherText){
                    sb.append(c.toChar())
                }
                data.letter = sb.toString()

                //Store the plant into Firestore:
                db.collection("user").document(localUidSample).collection("plants").document().set(
                    data
                )

                //Terminate the activity and return to the main screen:
                finish()
            }
            else {
                Snackbar.make(
                    view,
                    "Please, fill up all fields. Choosing a photo is optional.",
                    Snackbar.LENGTH_LONG
                )
                        .setAction("Action", null).show()
            }
        }
    }
}