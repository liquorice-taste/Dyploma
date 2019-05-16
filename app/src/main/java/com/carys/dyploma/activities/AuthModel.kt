package com.carys.dyploma.activities

import com.carys.dyploma.R
import com.carys.dyploma.activities.dataModels.HomeSystem
import com.carys.dyploma.activities.dataModels.LightController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthModel {
    fun func(callback: (ArrayList<HomeSystem> ) -> Unit) {
        GlobalScope.launch(Dispatchers.Main)
        {
            withContext(Dispatchers.Default) {
                val responseJson = JSONObject(
                    Requester.sendRequest(
                        //TODO get context here and retrieve from xml or not context.getString(R.string.auth_url),
                        "http://188.32.136.71:8000/api/system/",
                        "GET",
                        headers = arrayListOf("Authorization" to SharedUtils().read("Token"))
                    ).second
                )
                println(responseJson.getJSONArray("results"))
                responseJson.getJSONArray("results")
                try{
                    val systems = Gson().fromJson(
                        responseJson.getJSONArray("results").toString(),
                        Array<HomeSystem>::class.java
                    ).toCollection(ArrayList())
                    callback(systems)
                } catch (E: Exception) {
                    println("errar" + E.message)
                    //TODO: add error messageSystems
                }
            }
        }
    }



    fun checkCredentials(name: String, password: String, callback: (Int) -> Unit){
        val parameters: ArrayList<Pair<String, String>> = arrayListOf()
        parameters.add("username" to "pepe")
        parameters.add("password" to "pepepepe")
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val response = Requester.sendRequest(
                    "http://188.32.136.71:8000/api/token/",
                    "POST", parameters
                )
                if (response.second != "") {
                    val jobj = JSONObject(response.second)
                    if (jobj.has("access_token")) {
                        val utils = SharedUtils()
                        utils.write("Token", "Bearer " + jobj.getString("access_token"))
                    }
                }
                callback(response.first)
            }
        }
    }

}