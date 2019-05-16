package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.dataModels.Sensor
import kotlinx.android.synthetic.*
import org.jetbrains.anko.AnkoContext

class LightControllerAdapter(var list: ArrayList<LightController> = arrayListOf()) : RecyclerView.Adapter<LightControllerAdapter.LightControllerViewHolder>() {
    /*
    var lightlist : ArrayList<LightController>
    init {
        lightlist = list
    }
    */
    fun setData(lst: ArrayList<LightController>) {
        list = lst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightControllerViewHolder {
        return LightControllerViewHolder(LightControllerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: LightControllerViewHolder, position: Int) {
        val device = list[position]
        holder.lightBrightness.progress = device.lightBrightness
        holder.lightType.text = device.lightType
        //holder.lightRoom.text = device.lightRoom
        //holder.lightName.text = device.lightName
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LightControllerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lightBrightness: SeekBar
        var lightType: TextView
        //var lightRoom: TextView
        //var lightName: TextView

        init {
            lightBrightness = itemView.findViewById(LightControllerUI.lightBrightness)
            lightType = itemView.findViewById(LightControllerUI.lightType)
            //lightRoom = itemView.findViewById(DeviceItemUI.lightRoom)
            //lightName = itemView.findViewById(DeviceItemUI.lightName)
        }

    }

}