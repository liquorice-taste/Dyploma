package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.LightController
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Sensor

interface RoomContentCallback {
    fun onLightSuccess(callback: ResponseJSON<LightController>)
    fun onSensorSuccess(callback: ResponseJSON<Sensor>)
    fun onFailure(networkError: Throwable)
}