package com.example.labfinal

import android.app.Application
import android.content.Context

// Dung Pham 2217963 10.10.24
// Provides global application's context that MPDatabase can access
class MPApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}