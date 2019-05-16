package com.carys.dyploma.activities

import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.recyclerView.LightControllerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewPresenter(val view: MainViewActivity.MainViewActivityUi) {

    private val model = MainViewModel()
    fun func() {
        val list: ArrayList<LightController> = arrayListOf()
        model.getApiCall<LightController>("lightcontroller") {  list -> fulfillLightsList(list)  }
        //TODO DELETE
        view.devs.add(
            LightController(
                lightBrightness = 0,
                lightName = "dddddd",
                lightType = "Singlecolor"
            )
        )
        view.devs.add(
            LightController(
                lightBrightness = 14,
                lightName = "dddddd",
                lightType = "RGB"
            )
        )
        view.devs.add(
            LightController(
                lightBrightness = 250,
                lightName = "dddddd",
                lightType = "RGB"
            )
        )
    }

    fun fulfillLightsList(list: ArrayList<LightController>) = GlobalScope.launch(Dispatchers.Main) {
            view.devs = list
            val adapter = view.rec.adapter as LightControllerAdapter
            adapter.setData(list)

    }
}