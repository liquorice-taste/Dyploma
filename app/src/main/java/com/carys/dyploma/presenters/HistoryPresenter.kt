package com.carys.dyploma.presenters

import android.widget.ProgressBar
import com.carys.dyploma.activities.HistoryActivity
import com.carys.dyploma.callbacks.HistoryCallback
import com.carys.dyploma.dataModels.Token
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.models.AuthModel
import com.carys.dyploma.models.HistoryModel

class HistoryPresenter(private val view: HistoryActivity.HistoryActivityUI): HistoryCallback {
    override fun onJobSuccess(callback: Token) = with(view) {
        toast("history is here")
    }

    override fun onFailure(networkError: Throwable)  = with(view) {
        toast("couldn't load")
    }

    private val model = HistoryModel()

    fun getJobs() {
        model.getJobs(SharedUtils.read("Token"), this)
    }

}