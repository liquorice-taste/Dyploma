package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.Sensor
import kotlinx.android.synthetic.*
import org.jetbrains.anko.AnkoContext

class SensorAdapter(var list: ArrayList<Sensor> = arrayListOf()) : RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    fun setData(lst: ArrayList<Sensor>) {
        list = lst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        return SensorViewHolder(SensorUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val device = list[position]
        holder.sensCurrent_data.text = device.current_data.toString()
        holder.sensName.text = device.name
        holder.sensUnit_of_measure.text = device.unit_of_measure
        //holder.lightRoom.text = device.lightRoom
        //holder.lightName.text = device.lightName
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var sensCurrent_data: TextView
        var sensName: TextView
        var sensUnit_of_measure: TextView
        //var lightRoom: TextView
        //var lightName: TextView

        init {
            sensCurrent_data = itemView.findViewById(SensorUI.sensCurrent_data)
            sensName = itemView.findViewById(SensorUI.sensName)
            sensUnit_of_measure = itemView.findViewById(SensorUI.sensUnit_of_measure)
            //lightRoom = itemView.findViewByIdsensUnit_of_measure
            //lightName = itemView.findViewById(DeviceItemUI.lightName)
        }

    }

}