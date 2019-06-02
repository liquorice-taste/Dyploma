package com.carys.dyploma.general

import android.app.Application
import android.content.SharedPreferences

//TODO check if works
class SharedUtils : Application() {
    companion object {
        lateinit var preferences: SharedPreferences
        fun write(key: String, content: String) :Boolean = preferences.edit().putString(key, content).commit()
        fun remove(key: String) : Boolean = preferences.edit().remove(key).commit()
        fun removeAll(): Boolean {
            preferences.edit().clear().apply()
            return true
        }
        fun read(key:String) : String = preferences.getString(key, "")
    }
    override fun onCreate() {
        super.onCreate()
        preferences = this.baseContext.getSharedPreferences("", MODE_PRIVATE)

    }

}