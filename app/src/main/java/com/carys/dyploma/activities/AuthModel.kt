package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.util.Log
import com.carys.dyploma.activities.api.BigBrotherApi
import com.carys.dyploma.activities.dataModels.*
import io.reactivex.android.schedulers.AndroidSchedulers
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
    fun beginSearch(token: String, callback: AuthCallback ) {
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
