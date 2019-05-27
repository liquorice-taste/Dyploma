package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.HomeSystem
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Room
import com.carys.dyploma.activities.dataModels.Sensor
import com.carys.dyploma.activities.recyclerView.SensorAdapter

class MainViewPresenter(private val view: MainViewActivity.MainViewActivityUI): MainViewCallback {
    private val model = MainViewModel()

    fun getRooms() {
        model.getRooms(this, view.home.id)
    }

    fun getSystems() {
        model.beginSearch(this)
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