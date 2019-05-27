package com.carys.dyploma.activities.recyclerView

import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Responset
import com.carys.dyploma.activities.dataModels.Sensor

interface LightControllerCallback {
    fun onPutSuccess(callback: ResponseJSON<Responset>)

    fun onFailure(networkError: Throwable)
}