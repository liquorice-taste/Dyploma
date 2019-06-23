package com.carys.dyploma.presenters

import android.widget.ProgressBar
import com.carys.dyploma.activities.AuthActivity
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.dataModels.ResponseJSON
import com.carys.dyploma.dataModels.Token
import com.carys.dyploma.callbacks.AuthCallback
import com.carys.dyploma.models.AuthModel

class AuthPresenter(private val view: AuthActivity.AuthActivityUI): AuthCallback {

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
        //model.registerUser(view.username.text.toString(), view.password.text.toString(),SharedUtils.read("Token"), this)
        model.beginSearch(SharedUtils.read("Token"), this)
    }

    override fun onSystemSuccess(callback: ResponseJSON<HomeSystem>) {
        view.messageSystems(callback.results.toCollection(ArrayList()))
    }
}