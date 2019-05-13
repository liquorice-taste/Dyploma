package com.carys.dyploma.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthModel {
    fun checkCredentials(name: String, password: String, callback: (Int) -> Unit){
        val parameters: ArrayList<Pair<String, String>> = arrayListOf()
        parameters.add("username" to "pipi")
        parameters.add("password" to "pipipipi")
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val response = Requester.sendRequest(
                    "https://192.168.1.10:8000/api/token/",
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