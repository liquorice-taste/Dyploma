package com.carys.dyploma.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.carys.dyploma.R
import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.presenters.AuthPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUI().setContentView(this)
    }

    class AuthActivityUI() : AnkoComponent<AuthActivity> {

        private val presenter = AuthPresenter(this)
        lateinit var myui: AnkoContext<AuthActivity>
        lateinit var username: EditText
        lateinit var password: EditText
        lateinit var image: ImageView
        lateinit var login: Button
        lateinit var progress: ProgressBar

        private val customStyle = { v: Any ->
            when (v) {

                is Button -> v.textSize = myui.resources.getDimension(R.dimen.auth_button)
                is EditText -> v.textSize = myui.resources.getDimension(R.dimen.auth_text)
            }
        }

        override fun createView(ui: AnkoContext<AuthActivity>) = with(ui) {

            myui = ui
            verticalLayout {
                padding = dip(resources.getDimension(R.dimen.auth_margin))

                image = imageView(R.mipmap.bro).lparams {
                    margin = dip(resources.getDimension(R.dimen.auth_margin))
                    gravity = Gravity.CENTER
                }

                username = editText {
                    hintResource = R.string.username
                }
                password = editText {
                    hintResource = R.string.password
                    inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                }
                login = button {
                    text =  resources.getText(R.string.login_button)
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

        private fun launchActivity(home: HomeSystem) = with(myui) {
            val intent = Intent(ctx, MainViewActivity::class.java)
            intent.putExtra("HomeSystem", home)
            ctx.startActivity(intent)
            (ctx as Activity).finish()
        }

        fun toast(msg: String?) = with(myui) {
            if (msg != null) {
                ctx.toast(msg)
            }
        }

        fun messageSystems(list: ArrayList<HomeSystem>) = with(myui) {
            GlobalScope.launch(Dispatchers.Main) {
                selector(resources.getText(R.string.system_selector), list.map { it.name })
                    { _, i -> launchActivity(list[i]) }
            }
        }
    }
}