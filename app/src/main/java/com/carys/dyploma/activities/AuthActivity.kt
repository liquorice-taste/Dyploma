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
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUi(this.baseContext).setContentView(this)
    }

    class AuthActivityUi(context: Context) : AnkoComponent<AuthActivity> {

        val presenter = AuthPresenter(this)
        val cont = context
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
        fun launchActivity() = with(myui) {
            val intent = Intent(ctx, MainViewActivity::class.java)
            ctx.startActivity(intent)
            (ctx as Activity).finish()


        }
        fun toast(msg: String) {
            cont.toast(msg)
        }
    }
}