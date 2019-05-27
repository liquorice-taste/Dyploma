package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.HomeSystem
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Token

interface AuthCallback {
    fun onTokenSuccess(callback: Token)
    fun onFailure(networkError: Throwable)

    fun onSystemSuccess(callback: ResponseJSON<HomeSystem>)
}