package com.example.planttime.ui.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
        val time_created: LocalDate = LocalDate.now(),
        var creator: String = "",
        var expired: Boolean = false,
        val name: String = "",
        val opening: LocalDate = LocalDate.now())
{}
