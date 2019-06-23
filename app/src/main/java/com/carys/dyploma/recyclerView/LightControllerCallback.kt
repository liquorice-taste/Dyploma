package com.carys.dyploma.recyclerView

import com.carys.dyploma.dataModels.LightController
import com.carys.dyploma.dataModels.ResponseJSON

interface LightControllerCallback {
    fun onPutSuccess(callback: ResponseJSON<LightController>)
    fun onFailure(networkError: Throwable)
}