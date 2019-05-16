package com.carys.dyploma.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthPresenter(val view: AuthActivity.AuthActivityUi) {

    private val model = AuthModel()

    fun authorize() =
        GlobalScope.launch(Dispatchers.Main) {
            var respCode: Int = -1
            view.login.isEnabled = false
            model.checkCredentials(view.username.text.toString(), view.password.text.toString())
            { respCode -> uiResponse(respCode) }
        }

    fun getSystemsList() =
        GlobalScope.launch(Dispatchers.Main) {
            view.login.isEnabled = false
            model.checkCredentials(view.username.text.toString(), view.password.text.toString())
            { respCode -> uiResponse(respCode) }
        }
    fun uiResponse(responseCode: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            view.login.isEnabled = true
            when (responseCode) {
                200 -> {
                    model.func { list -> view.messageSystems(list) }
                }
                400 -> view.toast("Bad request")
                403 -> view.toast("Forbidden")
                404 -> view.toast("Page not found")
                408 -> view.toast("Request timed out")
                500 -> view.toast("Server error")
                else -> view.toast("Something bad happened")

            }
        }
    }
}