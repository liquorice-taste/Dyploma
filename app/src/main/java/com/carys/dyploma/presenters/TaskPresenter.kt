package com.carys.dyploma.presenters

import com.carys.dyploma.activities.TaskActivity
import com.carys.dyploma.dataModels.*
import com.carys.dyploma.callbacks.TaskCallback
import com.carys.dyploma.models.TaskModel

class TaskPresenter(private val view: TaskActivity.TaskActivityUI): TaskCallback {
    private val model = TaskModel()

    fun getLights() {
        model.getLightControllers(this, view.room.id)
    }

    fun getSensors() {
        model.getSensors(this, view.room.id)
    }

    override fun onLightSuccess(callback: ResponseJSON<LightController>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorSuccess(callback: ResponseJSON<Sensor>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(networkError: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}