package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Sensor
import com.carys.dyploma.activities.recyclerView.LightControllerAdapter
import com.carys.dyploma.activities.recyclerView.SensorAdapter

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