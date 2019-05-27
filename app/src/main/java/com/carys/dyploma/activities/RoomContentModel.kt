package com.carys.dyploma.activities

import android.annotation.SuppressLint
import com.carys.dyploma.activities.api.BigBrotherApi
import com.carys.dyploma.activities.dataModels.LightController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RoomContentModel {

    private val bigBrotherApi by lazy {
        BigBrotherApi.create()
    }

    @SuppressLint("CheckResult")
    fun getLightControllers(callback: RoomContentCallback, roomId: Int) {
        bigBrotherApi.getLightControllers(SharedUtils.read("Token"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onLightSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }

    @SuppressLint("CheckResult")
    fun getSensors(callback: RoomContentCallback, roomId: Int) {
        bigBrotherApi.getSensors(SharedUtils.read("Token"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    result -> callback.onSensorSuccess(result)
            }, {
                    error -> callback.onFailure(error)
            })
    }






    //TODO delete later
    inline fun <reified T: Any>  getApiCall(source: String, noinline callback: (ArrayList<T>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            var headers: ArrayList<Pair<String, String>> = arrayListOf()
            headers.add("Authorization" to SharedUtils.read("Token"))
            withContext(Dispatchers.Default) {
                val responseJson = JSONObject(
                    Requester.sendRequest(
                        "http://188.32.136.71:8000/api/$source",
                        "GET",
                        headers = arrayListOf("Authorization" to SharedUtils.read("Token"))
                    ).second
                )
                val listType = object : TypeToken<ArrayList<LightController>>(){}.type
                val list: ArrayList<T> = Gson().fromJson(responseJson.getJSONArray("results").toString(), listType)
                callback(list)
            }
        }

    }
}