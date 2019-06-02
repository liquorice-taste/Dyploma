package com.carys.dyploma.models

import android.annotation.SuppressLint
import com.carys.dyploma.callbacks.MainViewCallback
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.api.BigBrotherApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel {
    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    @SuppressLint("CheckResult")
    fun getRooms(callback: MainViewCallback, sysId: Int) {
        bigBrotherApi.getRooms(SharedUtils.read("Token"), sysId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onRoomSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }

    @SuppressLint("CheckResult")
    fun getSystems(callback: MainViewCallback) {
        bigBrotherApi.getSystems(SharedUtils.read("Token"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onSystemSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }
}