package com.carys.dyploma.activities

import android.widget.ProgressBar
import com.carys.dyploma.activities.dataModels.HomeSystem
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Token

class AuthPresenter(private val view: AuthActivity.AuthActivityUi): AuthCallback {

    private val model = AuthModel()

    fun authorize() {
        view.progress.visibility = ProgressBar.VISIBLE
        view.login.isEnabled = false
        model.getToken(view.username.text.toString(), view.password.text.toString(), this)
    }

    override fun onFailure(networkError: Throwable) = with(view){
        view.progress.visibility = ProgressBar.INVISIBLE
        login.isEnabled = true
        toast(networkError.message)
    }

    override fun onTokenSuccess(callback: Token) {
        view.progress.visibility = ProgressBar.INVISIBLE
        view.login.isEnabled = true
        SharedUtils.write("Token", "Bearer " + callback.access_token)
        model.beginSearch(SharedUtils.read("Token"), this)
    }

    override fun onSystemSuccess(callback: ResponseJSON<HomeSystem>) {
        view.messageSystems(callback.results.toCollection(ArrayList()))
    }
}