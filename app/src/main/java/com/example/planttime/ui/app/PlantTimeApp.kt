package com.example.planttime.ui.app

import android.app.Application
import android.content.Context

class PlantTimeApp: Application(){
    init {
        instance = this
    }

    companion object {
        private var instance: PlantTimeApp? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = PlantTimeApp.applicationContext()
    }
}