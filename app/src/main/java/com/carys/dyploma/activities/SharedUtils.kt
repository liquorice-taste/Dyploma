package com.carys.dyploma.activities

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

//TODO try changing to object
class SharedUtils : Application() {
    companion object{
        private lateinit var preferences :SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        preferences = this.applicationContext.getSharedPreferences("", MODE_PRIVATE)
    }
    fun write(key: String, content: String) :Boolean = preferences.edit().putString(key, content).commit()
    fun remove(key: String) : Boolean = preferences.edit().remove(key).commit()
    fun removeAll(): Boolean {
        preferences.edit().clear().apply()
        return true
    }
    fun read(key:String) : String = preferences.getString(key, "")
}