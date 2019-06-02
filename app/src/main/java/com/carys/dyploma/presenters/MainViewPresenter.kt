package com.carys.dyploma.presenters

import com.carys.dyploma.activities.MainViewActivity
import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Room
import com.carys.dyploma.callbacks.MainViewCallback
import com.carys.dyploma.models.MainViewModel

class MainViewPresenter(private val view: MainViewActivity.MainViewActivityUI):
    MainViewCallback {
    private val model = MainViewModel()

    fun getRooms() {
        model.getRooms(this, view.home.id)
    }

    fun getSystems() {
        model.getSystems(this)
    }

    override fun onRoomSuccess(callback: ResponseJSON<Room>) {
        view.pagerAdapter.refreshRoomList(callback.results)
    }

    override fun onSystemSuccess(callback: ResponseJSON<HomeSystem>) {
        view.messageSystems(callback.results.toCollection(ArrayList()))
    }

    override fun onFailure(networkError: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}