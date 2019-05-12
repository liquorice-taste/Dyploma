package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.recyclerView.LightControllerAdapter
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.json.JSONObject

class MainViewActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(this)
        MainViewActivityUi(linearLayoutManager).setContentView(this)
    }

    class MainViewActivityUi(linearLayoutManager: LinearLayoutManager) : AnkoComponent<MainViewActivity> {
        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = 26f
                is EditText -> v.textSize = 24f
                is TextView -> v.textSize = 26f
            }
        }
        val ll = linearLayoutManager

        @SuppressLint("SetTextI18n")
        override fun createView(ui: AnkoContext<MainViewActivity>) = with(ui) {
            verticalLayout {
                textView() {
                    text = "Lights"
                }
                var devs: ArrayList<LightController> = arrayListOf()
                GlobalScope.launch(Dispatchers.Main) {
                    var headers: ArrayList<Pair<String, String>> = arrayListOf()
                    headers.add("Authorization" to Token.token.token_header)
                    withContext(Dispatchers.Default) {
                        val responseJson = JSONObject(
                            Requester.sendRequest(
                                "http://192.168.1.10:8000/api/lightcontroller/",
                                "GET",
                                headers = headers
                            ).second
                        )
                        println(responseJson.getJSONArray("results"))
                        responseJson.getJSONArray("results")
                        val gson = Gson()
                            devs = gson.fromJson(
                                responseJson.getJSONArray("results").toString(),
                                Array<LightController>::class.java
                            ).toCollection(ArrayList())
                            devs.add(
                                LightController(
                                    lightBrightness = 0,
                                    lightName = "dddddd",
                                    lightType = "Singlecolor"
                                )
                            )
                            devs.add(
                                LightController(
                                    lightBrightness = 14,
                                    lightName = "dddddd",
                                    lightType = "RGB"
                                )
                            )
                            devs.add(
                                LightController(
                                    lightBrightness = 250,
                                    lightName = "dddddd",
                                    lightType = "RGB"
                                )
                            )
                        }
                        recyclerView {
                            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                            adapter = LightControllerAdapter(devs)

                        }
                    }
                bottomNavigationView {

                }
            }.applyRecursively(customStyle)
        }
    }
}