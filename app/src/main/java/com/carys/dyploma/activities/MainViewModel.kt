package com.carys.dyploma.activities

import com.carys.dyploma.R
import com.carys.dyploma.activities.dataModels.LightController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainViewModel {

    inline fun <reified T: Any>  getApiCall(source: String, noinline callback: (ArrayList<T> ) -> Unit) {
        GlobalScope.launch(Dispatchers.Main)
        {
            var headers: ArrayList<Pair<String, String>> = arrayListOf()
            headers.add("Authorization" to SharedUtils().read("Token"))
            withContext(Dispatchers.Default) {
                val responseJson = JSONObject(
                    Requester.sendRequest(
                        "http://188.32.136.71:8000/api/$source",
                        "GET",
                        headers = arrayListOf("Authorization" to SharedUtils().read("Token"))
                    ).second
                )
                println("вот ответ" + responseJson.getJSONArray("results"))
                responseJson.getJSONArray("results")
                val gson = Gson()

                callback(gson.fromJson(
                    responseJson.getJSONArray("results").toString(),
                    Array<T>::class.java
                ).toCollection(ArrayList()))
            }

        }
    }
}