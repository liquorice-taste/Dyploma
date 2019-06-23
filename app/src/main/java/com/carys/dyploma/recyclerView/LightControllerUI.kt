package com.carys.dyploma.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Switch
//import kotlinx.android.synthetic.main.activity_main.view.*
import org.jetbrains.anko.*

class LightControllerUI : AnkoComponent<ViewGroup> {
    companion object {
        const val lightType = 1
        const val lightBrightness = 2
        val lightId = 3
        val lightName = 4
        val lightUuid = 5
        val lightPort = 6
        val lightStatus = 7
        val lightSystem = 8
        val lightRoom = 9
        const val lightSwitch = 10
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

                }.lparams { alignParentRight() }
                brightness = seekBar {
                    id = lightBrightness
                    max = 255

                }.lparams{ width = matchParent; leftOf(lightSwitch) }

            }
            padding = dip(16)
        }
    }
}

