package com.carys.dyploma.activities.recyclerView

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
//import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onSeekBarChangeListener

class LightControllerUI : AnkoComponent<ViewGroup> {
    companion object {
        val lightType = 1
        const val lightBrightness = 2
        val lightId = 3
        val lightName = 4
        val lightUuid = 5
        val lightPort = 6
        val lightStatus = 7
        val lightSystem = 8
        val lightRoom = 9
        val lightSwitch = 10
    }

    lateinit var lswitch: Switch
    lateinit var brightness: SeekBar

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui){
        brightness = SeekBar(ctx)
        verticalLayout {

            lparams(matchParent, wrapContent)
            val lType = textView {
                id = lightType
            }
            relativeLayout {
                lparams(matchParent, wrapContent)

                lswitch = switch {
                    id = lightSwitch
                    gravity = Gravity.RIGHT
                    onCheckedChange { buttonView, isChecked ->
                        brightness.progress = when(isChecked) {
                            false -> 0
                            true -> brightness.max
                        }
                    }
                }.lparams { alignParentRight() }
                brightness = seekBar {
                    id = lightBrightness
                    max = 255


                    //TODO: check if works
                    onSeekBarChangeListener {
                        onProgressChanged { seekBar, i, b ->
                            lswitch.isChecked = when (i){
                                0 -> false
                                else -> true
                            }

                        }
                    }
                }.lparams{ width = matchParent; leftOf(lightSwitch) }

            }

            padding = dip(16)

            /*when (lType.text) {
                "RGB" -> {

                }
                "Singlecolor" -> {
                    seekBar {
                        //id = lightBrightness
                        max = 255
                        onSeekBarChangeListener {}
                    }
                }

            }*/
        }
    }
}

