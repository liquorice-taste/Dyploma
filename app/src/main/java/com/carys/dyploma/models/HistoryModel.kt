package com.carys.dyploma.models

import com.carys.dyploma.api.BigBrotherApi
import com.carys.dyploma.callbacks.HistoryCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryModel {
    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    fun getJobs(token: String, callback: HistoryCallback) {
        with(bigBrotherApi) {
            getJobs(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    callback.onJobSuccess(result)
                }, { error ->
                    callback.onFailure(error)
                })
        }
    }
}