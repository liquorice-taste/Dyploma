package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
//import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onSeekBarChangeListener

class LightControllerUI : AnkoComponent<ViewGroup> {
    companion object {
        val lightType = 1
        val lightBrightness = 2
        val lightId = 3
        val lightName = 4
        val lightUuid = 5
        val lightPort = 6
        val lightStatus = 7
        val lightSystem = 8
        val lightRoom = 9
    }
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui){
        verticalLayout {
            lparams(matchParent, wrapContent)
            val lType = textView {
                id = lightType
            }
            seekBar {
                id = lightBrightness
                max = 255
                onSeekBarChangeListener {}
            }
            padding = dip(16)
            when (lType.text) {
                "RGB" -> {


                }
                "Singlecolor" -> {
                    seekBar {
                        //id = lightBrightness
                        max = 255
                        onSeekBarChangeListener {}
                    }
                }

            }
        }
    }
}


class DeviceItemUI : AnkoComponent<ViewGroup> {
    companion object {
        val lightType = 1
        val lightBrightness = 2
        val lightId = 3
        val lightName = 4
        val lightUuid = 5
        val lightPort = 6
        val lightStatus = 7
        val lightSystem = 8
        val lightRoom = 9
    }
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {

            linearLayout {

                lparams(matchParent, wrapContent)
                padding = dip(16)
                seekBar {
                    id = LightControllerUI.lightBrightness
                    max = 255
                    onSeekBarChangeListener {
                        /*GlobalScope.launch(Dispatchers.Main) {
                            var headers: ArrayList<Pair<String, String>> = arrayListOf()
                            headers.add("Authorization" to Token.token.token_header)
                            withContext(Dispatchers.Default) {
                                val responseJson = JSONObject(
                                    Requester.sendRequest(
                                        "http://192.168.1.10:8000/api/lightcontroller/",
                                        "POST",
                                        headers = headers
                                    ).second
                                )
                            }
                        }*/
                    }
                }
            }

    }
}