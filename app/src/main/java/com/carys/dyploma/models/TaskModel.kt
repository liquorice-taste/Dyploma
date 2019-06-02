package com.carys.dyploma.models

import android.annotation.SuppressLint
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.callbacks.TaskCallback
import com.carys.dyploma.api.BigBrotherApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskModel {
    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    @SuppressLint("CheckResult")
    fun getLightControllers(callback: TaskCallback, roomId: Int) {
        bigBrotherApi.getLightControllers(SharedUtils.read("Token"), roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onLightSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }

    @SuppressLint("CheckResult")
    fun getSensors(callback: TaskCallback, roomId: Int) {
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