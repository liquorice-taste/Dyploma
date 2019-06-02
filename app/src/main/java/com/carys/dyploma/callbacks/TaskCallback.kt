package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.*

interface TaskCallback {
    fun onLightSuccess(callback: ResponseJSON<LightController>)
    fun onSensorSuccess(callback: ResponseJSON<Sensor>)
    fun onFailure(networkError: Throwable)
}