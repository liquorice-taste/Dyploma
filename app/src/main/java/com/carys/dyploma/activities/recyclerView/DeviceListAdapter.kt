package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.LightController
import org.jetbrains.anko.AnkoContext
import com.carys.dyploma.activities.dataModels.Sensor

class DeviceListAdapter(var list: ArrayList<LightController> = arrayListOf()) : RecyclerView.Adapter<DeviceListAdapter.LightControllerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightControllerViewHolder {
        return LightControllerViewHolder(LightControllerUI().createView(AnkoContext.create(parent.context, parent)))
    }
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onBindViewHolder(holder: LightControllerViewHolder, position: Int) {
        val device = list?.get(position)
        holder.lightType.text = device.lightType
        holder.lightBrightness.progress = device.lightBrightness
/*
        inline fun LinearLayoutManager.recyclerView(init: RecyclerView.() -> Unit = {}) : RecyclerView {
            return ankoView({RecyclerView(holder.devList.context)}, theme = 0, init = init)
        }


        val itemLayoutManager = LinearLayoutManager(
            holder.devList.context, LinearLayout.HORIZONTAL, false
        )

        //childLayoutManager.initialPrefetchItemCount = 4

        holder.devList.apply {
            layoutManager = itemLayoutManager
            adapter = LightControllerAdapter(device.second)
            //setRecycledViewPool(viewPool)
        }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LightControllerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lightType: TextView
        var lightBrightness: SeekBar
        //var devList: List<Sensor> = listOf()

        init {
            lightType = itemView.findViewById(LightControllerUI.lightType)
            lightBrightness = itemView.findViewById(LightControllerUI.lightBrightness)
        }

    }
}