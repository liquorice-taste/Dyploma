package com.carys.dyploma.models

import android.annotation.SuppressLint
import com.carys.dyploma.callbacks.AuthCallback
import com.carys.dyploma.api.BigBrotherApi
import com.carys.dyploma.dataModels.*
import io.reactivex.android.schedulers.AndroidSchedulers.*
import io.reactivex.schedulers.Schedulers

class AuthModel {

    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    fun getToken(username: String, password: String, callback: AuthCallback) {
        with(bigBrotherApi) {
            getToken(Credentials("pepe", "pepepepe"))
                //.map{Log.d("gettoken", Thread.currentThread().name)}
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .subscribe({
                    result -> callback.onTokenSuccess(result)
                }, {
                    error -> callback.onFailure(error)
                })
        }
    }

    @SuppressLint("CheckResult")
    fun beginSearch(token: String, callback: AuthCallback) {
        bigBrotherApi.getSystems(token)
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread())
            .subscribe({
                    result -> callback.onSystemSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }


}
