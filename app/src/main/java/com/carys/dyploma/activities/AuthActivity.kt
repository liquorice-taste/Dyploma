package com.carys.dyploma.activities

import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.carys.dyploma.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlinx.coroutines.*
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUi().setContentView(this)
    }

    class AuthActivityUi : AnkoComponent<AuthActivity> {


        var activity = this
        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = 26f
                is EditText -> v.textSize = 24f
            }
        }
        override fun createView(ui: AnkoContext<AuthActivity>) = with(ui) {

            verticalLayout {
                padding = dip(32)

                imageView(R.mipmap.bro).lparams {
                    margin = dip(32)
                    gravity = Gravity.CENTER
                }

                val name = editText {
                    hintResource = R.string.username
                }
                val password = editText {
                    hintResource = R.string.password
                    inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                }


                button("Log in") {
                    fun uiResponse(responseCode: Int) {
                            println("ui: " + Thread.currentThread().name)
                            when (responseCode) {
                                200 -> startActivity<MainViewActivity>()
                                400 -> toast("Bad request")
                                403 -> toast("Forbidden")
                                404 -> toast("Page not found")
                                408 -> toast("Request timed out")
                                500 -> toast("Server error")
                                else -> toast("something bad happend")

                        }
                    }
                    onClick() {
                        GlobalScope.launch(Dispatchers.Main) {
                            var respCode: Int = -1
                            isEnabled = false
                            println("check1: " + Thread.currentThread().name)
                            withContext(Dispatchers.Default) {
                                respCode = checkCredentials(name.text.toString(), password.text.toString())
                                println("check: " + Thread.currentThread().name)
                            //TODO: check if it's safe and maybe drop function
                            //delay(300)
                            }
                            isEnabled = true
                            uiResponse(respCode)

                        }
                    }
                }


            }.applyRecursively(customStyle)

        }

        private fun checkCredentials(name: String, password: String): Int {

            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger()
            var parameters: ArrayList<Pair<String, String>> = arrayListOf()
            parameters.add("username" to "pipi")
            parameters.add("password" to "pipipipi")
            val response = Requester.sendRequest("http://192.168.1.10:8000/api/token-auth/",
                "POST",  parameters)
            if (response.second != "") {
                val t = JSONObject(response.second)
                if (t.has("token"))
                    Token.token.token_header = "Token " + t.getString("token")
            }
            return response.first
        }


    }
}