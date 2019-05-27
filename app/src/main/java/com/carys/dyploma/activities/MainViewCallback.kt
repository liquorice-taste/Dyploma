package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.HomeSystem
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Room

interface MainViewCallback {
    fun onRoomSuccess(callback: ResponseJSON<Room>)
    fun onSystemSuccess(callback: ResponseJSON<HomeSystem>)
    fun onFailure(networkError: Throwable)
}