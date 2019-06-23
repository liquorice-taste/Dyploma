package com.carys.dyploma.models

import android.annotation.SuppressLint
import com.carys.dyploma.callbacks.RoomContentCallback
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.api.BigBrotherApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RoomContentModel {

    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    fun getLightControllers(callback: RoomContentCallback, roomId: Int) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(bigBrotherApi.getLightControllers(SharedUtils.read("Token"), roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onLightSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            }))
        compositeDisposable.dispose()
    }

    @SuppressLint("CheckResult")
    fun getSensors(callback: RoomContentCallback, roomId: Int) {
        bigBrotherApi.getSensors(SharedUtils.read("Token"), roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onSensorSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }
}