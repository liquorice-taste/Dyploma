package com.carys.dyploma.activities.recyclerView

import android.annotation.SuppressLint
import com.carys.dyploma.activities.SharedUtils
import com.carys.dyploma.activities.api.BigBrotherApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LightControllerModel {

    val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    @SuppressLint("CheckResult")
    fun putLamp(callback: LightControllerCallback, controller: Int, brightness: Int) {
        bigBrotherApi.putLightControllers(SharedUtils.read("Token"), brightness, controller)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onPutSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }
}