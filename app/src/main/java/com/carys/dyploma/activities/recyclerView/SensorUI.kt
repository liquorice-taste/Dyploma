package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.verticalLayout


class SensorUI : AnkoComponent<ViewGroup> {
    companion object {
        val sensId = 1
        val sensName = 2
        val sensUuid = 3
        val sensPort = 4
        val sensStatus = 5
        val sensCurrent_data = 6
        val sensUnit_of_measure =7
        val sensSystem = 8
        val sensRoom = 9
    }
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {

        }
    }
}