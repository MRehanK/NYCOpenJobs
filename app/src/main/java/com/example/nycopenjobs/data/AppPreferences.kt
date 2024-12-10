package com.example.nycopenjobs.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.nycopenjobs.util.Tag

interface AppSharedPreferences {
    fun getSharedPreferences(): SharedPreferences
}

class AppPreferences(private val context: Context) : AppSharedPreferences {

    private val prefsKey = "prefs"

    override fun getSharedPreferences(): SharedPreferences {
        Log.i(Tag, "Getting shared preferences")
        return context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
    }
}
