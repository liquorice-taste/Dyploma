package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.R
import com.carys.dyploma.dataModels.LightController
import com.carys.dyploma.dataModels.Sensor
import com.carys.dyploma.recyclerView.LightControllerAdapter
import com.carys.dyploma.recyclerView.SensorAdapter
import com.carys.dyploma.presenters.RoomContentPresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI

class RoomContentActivity(contentLayoutId: Int, val roomId: Int) : Fragment(contentLayoutId) {
    companion object {
        const val lightRecycler = 1
        const val sensorRecycler = 2
    }

    private val presenter: RoomContentPresenter by lazy{
        RoomContentPresenter(this)
    }
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
            is TextView -> v.textSize = 26f

        }
    }

    lateinit var lightRec: RecyclerView
    lateinit var sensorRec: RecyclerView

    private var lights: ArrayList<LightController> = arrayListOf()
    private var sensors: ArrayList<Sensor> = arrayListOf()
    //private lateinit var pagerAdapter: PagerAdapter

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.getLights()
        presenter.getSensors()
        return UI {
            //id = 100
            //layoutParam(width = matchParent, height = matchParent)
            verticalLayout {
                textView() {
                    text = resources.getText(R.string.lights)
                }
                lightRec = recyclerView {
                    id = lightRecycler
                    layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
                    adapter = LightControllerAdapter(lights)
                }
                sensorRec = recyclerView {
                    id = sensorRecycler
                    layoutManager = LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false)
                    adapter = SensorAdapter(sensors)
                }
                //bottomNavigationView { }
            }.applyRecursively(customStyle)
        }.view
    }
}