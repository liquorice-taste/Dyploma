package com.carys.dyploma.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.carys.dyploma.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlinx.coroutines.*
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUi(this.baseContext).setContentView(this)
    }

    lateinit var name: EditText
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
                        presenter.callServer()
                    }
                }
            }.applyRecursively(customStyle)
        }
        fun launchActivity() = with(myui) {
            val intent = Intent(ctx, MainViewActivity::class.java)
            intent.putExtra("id", 5)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            ctx.startActivity(intent)
        }
        fun toast(msg: String) {
            cont.toast(msg)
        }
    }
}