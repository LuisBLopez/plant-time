package com.example.planttime

import android.app.DatePickerDialog
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlantBinding
    private val localUidSample = "RzZU71c31Zmi3vCiHbsC" //"l4VBLVnZeN1M7fMMhee8" //Placeholder user Id. This will later be modified whenever we implement the Log-in operations.
    private lateinit var db: FirebaseFirestore
    private var aes = AES()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)//(R.layout.activity_add_plant)

        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        onViewCreated(binding.root, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var set_day = 0
        var set_month = 0
        var set_year = -1

        binding.plantExpiration.setOnClickListener {
            Snackbar.make(view, "Choose a date when the plant will bloom", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val datePickerFrag = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener{ _, year, month, day ->
                // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year

                set_day = day
                set_month = month
                set_year = year

                binding.plantExpiration.setText(selectedDate)
            })
            datePickerFrag.show(supportFragmentManager, "datePicker")
        }

        binding.plantConfirmAdd.setOnClickListener {
            val letter = binding.plantLetter.text
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val strPicUrl = sharedPref.getString("chosen_pic_url", "No_photo_chosen_yet")

            if (!binding.plantName.text.isNullOrEmpty() && !binding.plantExpiration.text.isNullOrEmpty() && !letter.isNullOrEmpty()) {
                Snackbar.make(view, "Creating a new plant...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                println("STARTING SETTING STUFF MAYBE")

                val data = Plant(Date(),localUidSample,false,binding.plantName.text.toString(),Date(set_year-1900,set_month,set_day))

                if (strPicUrl != "No_photo_chosen_yet") data.mediaRef = strPicUrl

                sharedPref.edit().putString("chosen_pic_url", "No_photo_chosen_yet").apply()

                val context = PlantTimeApp.applicationContext()
                val cipherText = aes.encrypt(context, letter.toString())
                val sb = StringBuilder()
                for(c in cipherText){
                    sb.append(c.toChar())
                }
                println("cipherText: ${String(cipherText)}")
                val decryptedText = aes.decrypt(context, cipherText)

                println("decrypted text: ${String(decryptedText)}")
                data.letter = sb.toString()
                db.collection("user").document(localUidSample).collection("plants").document().set(data)

                println("DONE SETTING STUFF MAYBE")
                finish()
            }
            else {
                Snackbar.make(view, "Please, fill up all fields. Choosing a photo is optional.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }
}