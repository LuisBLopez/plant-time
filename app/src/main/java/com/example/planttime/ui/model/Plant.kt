package com.example.planttime.ui.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
    val creation: Date,
    var creator: String,
    var expired: Boolean = false,
    val name: String,
    val opening: Date
)
{
    //Empty constructor:
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(Date(),"", false, "", Date())

}
/*    val id: Int = 0,
    val name: String = "",
    val time_created: LocalDate = LocalDate.now()
){
    val isExpired: Boolean
        get() = false
}
*/