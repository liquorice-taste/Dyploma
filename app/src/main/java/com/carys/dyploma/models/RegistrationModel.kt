package com.carys.dyploma.models

import com.carys.dyploma.api.BigBrotherApi
import com.carys.dyploma.callbacks.AuthCallback
import com.carys.dyploma.callbacks.RegistrationCallback
import com.carys.dyploma.dataModels.Credentials
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegistrationModel {

    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    fun registerUser(username: String, password: String, callback: RegistrationCallback) {
        with(bigBrotherApi) {
            registerUser(Credentials(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    callback.onRegisterSuccess(result)
                }, { error ->
                    callback.onFailure(error)
                })
        }
    }
}