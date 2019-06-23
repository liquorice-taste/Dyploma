package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Room

interface MainViewCallback {
    fun onRoomSuccess(callback: ResponseJSON<Room>)
    fun onSystemSuccess(callback: ResponseJSON<HomeSystem>)

    fun onFailure(networkError: Throwable)
}