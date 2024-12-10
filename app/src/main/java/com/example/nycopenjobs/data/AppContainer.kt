package com.example.nycopenjobs.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.nycopenjobs.api.AppRemoteApis

interface AppContainer {
    val appRepository: AppRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val appRepository: AppRepository by lazy {
        Log.i(TAG, "initializing app repository")
        AppRepositoryImpl(
            AppRemoteApis().getNycOpenDataApi(),
            AppPreferences(context).getSharedPreferences(),
            LocalDatabase.getDatabase(context).jobPostDao()
        )
    }
}
