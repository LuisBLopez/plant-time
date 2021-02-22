package com.example.planttime.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int = 0,
    val name: String = "",
    val time_left: LocalDate = LocalDate.now()
){
    val isExpired: Boolean
        get() = false
}
