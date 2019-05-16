package com.carys.dyploma.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.carys.dyploma.R
import com.carys.dyploma.activities.dataModels.HomeSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUi().setContentView(this)
    }

    class AuthActivityUi() : AnkoComponent<AuthActivity> {

        val presenter = AuthPresenter(this)
        lateinit var myui: AnkoContext<AuthActivity>
        lateinit var username: EditText
        lateinit var password: EditText
        lateinit var image: ImageView
        lateinit var login: Button

        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = 26f
                is EditText -> v.textSize = 24f
            }
        }

        override fun createView(ui: AnkoContext<AuthActivity>) = with(ui) {

            myui = ui
            verticalLayout {
                padding = dip(32)

                image = imageView(R.mipmap.bro).lparams {
                    margin = dip(32)
                    gravity = Gravity.CENTER
                }

                username = editText {
                    hintResource = R.string.username
                }
                password = editText {
                    hintResource = R.string.password
                    inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                }
                login = button("Log in") {
                    onClick() {
                        presenter.authorize()
                    }
                }
            }.applyRecursively(customStyle)
        }

        fun launchActivity(home: HomeSystem) = with(myui) {
            val intent = Intent(ctx, MainViewActivity::class.java)
            intent.putExtra("HomeSystem", home)
            ctx.startActivity(intent)
            (ctx as Activity).finish()
        }

        fun toast(msg: String) = with(myui) {
            ctx.toast(msg)
        }

        fun messageSystems(list: ArrayList<HomeSystem>) = with(myui) {
            GlobalScope.launch(Dispatchers.Main) {
                selector("Where are you from?", list.map { it.name })
                    { dialogInterface, i -> launchActivity(list[i]) }
            }
        }
    }
}