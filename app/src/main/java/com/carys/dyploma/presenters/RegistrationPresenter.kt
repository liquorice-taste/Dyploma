package com.carys.dyploma.presenters

import android.widget.ProgressBar
import com.carys.dyploma.R
import com.carys.dyploma.activities.AuthActivity
import com.carys.dyploma.activities.RegistrationActivity
import com.carys.dyploma.callbacks.RegistrationCallback
import com.carys.dyploma.dataModels.Token
import com.carys.dyploma.models.AuthModel
import com.carys.dyploma.models.RegistrationModel
import org.jetbrains.anko.resources
import org.jetbrains.anko.toast

class RegistrationPresenter(private val view: RegistrationActivity.RegistrationActivityUI): RegistrationCallback {
    private val model = RegistrationModel()

    fun authorize() {
        view.progress.visibility = ProgressBar.VISIBLE
        view.register.isEnabled = false

        model.registerUser(view.username.text.toString(), view.password.text.toString(), this)
    }

    override fun onRegisterSuccess(callback: Token) = with(view) {
        view.progress.visibility = ProgressBar.INVISIBLE
        view.register.isEnabled = true
        toast(view.myui.ctx.resources.getString(R.string.on_reg_success))
    }

    override fun onFailure(networkError: Throwable) = with(view)  {
        view.progress.visibility = ProgressBar.INVISIBLE
        view.register.isEnabled = true
        toast(view.myui.ctx.resources.getString(R.string.on_reg_failure))
    }
}