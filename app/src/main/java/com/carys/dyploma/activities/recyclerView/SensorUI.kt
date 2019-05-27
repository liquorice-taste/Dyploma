package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView


class SensorUI : AnkoComponent<ViewGroup> {
    companion object {
        val sensId = 1
        const val sensName = 2
        val sensUuid = 3
        val sensPort = 4
        val sensStatus = 5
        const val sensCurrent_data = 6
        const val sensUnit_of_measure = 7
        val sensSystem = 8
        val sensRoom = 9
    }
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {

        cardView {
            lparams{ matchParent; setMargins(50, 50,50, 50)}
            relativeLayout {
                textView {
                    id = sensName
                }
                textView {
                    id = sensCurrent_data
                }.lparams{below(sensName)}
                textView {
                    id = sensUnit_of_measure
                }.lparams{rightOf(sensCurrent_data); below(sensName)}

            }.lparams(matchParent, wrapContent)
        }
    }
}