package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Responset
import com.carys.dyploma.activities.dataModels.Sensor

interface RoomContentCallback {
    fun onLightSuccess(callback: ResponseJSON<LightController>)
    fun onSensorSuccess(callback: ResponseJSON<Sensor>)
    fun onFailure(networkError: Throwable)
}