package com.carys.dyploma.activities

import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject

class AuthPresenter(val activity: AuthActivity.AuthActivityUi) {

    fun callServer(){
        GlobalScope.launch(Dispatchers.Main) {
            var respCode: Int = -1
            activity.login.isEnabled = false
            withContext(Dispatchers.Default) {
                respCode = checkCredentials(activity.username.text.toString(), activity.password.text.toString())
                //TODO: check if it's safe and maybe drop function
                //delay(300)
            }
            activity.login.isEnabled = true
            uiResponse(respCode)
        }
    }
    fun uiResponse(responseCode: Int) {
        println("ui: " + Thread.currentThread().name)
        when (responseCode) {
            200 -> activity.launchActivity()
            400 -> activity.toast("Bad request")
            403 -> activity.toast("Forbidden")
            404 -> activity.toast("Page not found")
            408 -> activity.toast("Request timed out")
            500 -> activity.toast("Server error")
            else -> activity.toast("Something bad happened")

        }
    }
    private fun checkCredentials(name: String, password: String): Int {
        var parameters: ArrayList<Pair<String, String>> = arrayListOf()
        parameters.add("username" to "pipi")
        parameters.add("password" to "pipipipi")
        val response = Requester.sendRequest("https://192.168.1.10:8000/api/token/",
            "POST",  parameters)
        if (response.second != "") {
            val jobj = JSONObject(response.second)
            if (jobj.has("access_token")){
                val utils = SharedUtils()
                utils.write("Token", "Bearer " +  jobj.getString("access_token"))
            }
        }
        return response.first
    }
}