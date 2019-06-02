package com.carys.dyploma.presenters

import com.carys.dyploma.activities.RoomContentActivity
import com.carys.dyploma.dataModels.LightController
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Sensor
import com.carys.dyploma.recyclerView.LightControllerAdapter
import com.carys.dyploma.recyclerView.SensorAdapter
import com.carys.dyploma.callbacks.RoomContentCallback
import com.carys.dyploma.models.RoomContentModel

class RoomContentPresenter(private val view: RoomContentActivity): RoomContentCallback {
    private val model = RoomContentModel()

    fun getLights() {
        model.getLightControllers(this, view.roomId)
    }

    fun getSensors() {
        model.getSensors(this, view.roomId)
    }

    override fun onSensorSuccess(callback: ResponseJSON<Sensor>) {
        val adapter = view.sensorRec.adapter as SensorAdapter
        adapter.setData(callback.results.toCollection(ArrayList<Sensor>()))
    }

    override fun onLightSuccess(callback: ResponseJSON<LightController>) {
        val adapter = view.lightRec.adapter as LightControllerAdapter
        adapter.setData(callback.results.toCollection(ArrayList<LightController>()))
    }

    override fun onFailure(networkError: Throwable){
    }

}