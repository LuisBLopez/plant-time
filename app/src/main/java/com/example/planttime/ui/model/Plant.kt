package com.example.planttime.ui.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
        val creation: Date? = null,
        var creator: String = "",
        var expired: Boolean = false,
        val name: String = "",
        val opening: Date? = null)
{}
