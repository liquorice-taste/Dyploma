package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Token

interface HistoryCallback {
    fun onJobSuccess(callback: Token)

    fun onFailure(networkError: Throwable)
}