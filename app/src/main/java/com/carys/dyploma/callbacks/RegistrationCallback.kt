package com.carys.dyploma.callbacks

import com.carys.dyploma.dataModels.Token

interface RegistrationCallback {
    fun onRegisterSuccess(callback: Token)

    fun onFailure(networkError: Throwable)

}