package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Token

interface AuthCallback {
    fun onTokenSuccess(callback: Token)
    fun onFailure(networkError: Throwable)

    fun onSystemSuccess(callback: ResponseJSON<HomeSystem>)
}