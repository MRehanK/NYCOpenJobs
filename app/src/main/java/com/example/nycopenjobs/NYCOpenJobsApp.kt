package com.example.nycopenjobs

import android.app.Application
import com.example.nycopenjobs.data.AppContainer
import com.example.nycopenjobs.data.DefaultAppContainer

class NYCOpenJobsApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
