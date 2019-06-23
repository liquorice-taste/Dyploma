package com.carys.dyploma.activities

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.carys.dyploma.R
import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.presenters.MainViewPresenter
import com.carys.dyploma.presenters.RegistrationPresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class RegistrationActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RegistrationActivityUI().setContentView(this)
    }

    class RegistrationActivityUI : AnkoComponent<RegistrationActivity> {

        lateinit var myui: AnkoContext<RegistrationActivity>
        lateinit var username: EditText
        lateinit var password: EditText
        lateinit var register: Button
        lateinit var progress: ProgressBar
        private val presenter = RegistrationPresenter(this)

        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = myui.resources.getDimension(R.dimen.auth_button)
                is EditText -> v.textSize = myui.resources.getDimension(R.dimen.auth_text)
            }
        }
        override fun createView(ui: AnkoContext<RegistrationActivity>): View  = with(ui) {
            myui = ui
            verticalLayout {
                padding = dip(resources.getDimension(R.dimen.auth_margin))

                username = editText {
                    hintResource = R.string.username
                }
                password = editText {
                    hintResource = R.string.password
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                register = button {
                    text = resources.getText(R.string.login_button)
                    onClick() {
                        presenter.authorize()
                    }
                }
                progress = progressBar {
                    incrementProgressBy(5)
                    visibility = ProgressBar.INVISIBLE
                }



            }.applyRecursively(customStyle)
        }
        fun toast(msg: String?) = with(myui) {
            if (msg != null) {
                ctx.toast(msg)
            }
        }

    }
}
