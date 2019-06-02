package com.carys.dyploma.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.dataModels.LightController
import com.carys.dyploma.dataModels.ResponseJSON
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onSeekBarChangeListener

class LightControllerAdapter(var list: ArrayList<LightController> = arrayListOf()) : LightControllerCallback, RecyclerView.Adapter<LightControllerAdapter.LightControllerViewHolder>() {

    private val model = LightControllerModel()

    fun setData(lst: ArrayList<LightController>) {
        list = lst
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightControllerViewHolder {
        return LightControllerViewHolder(LightControllerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: LightControllerViewHolder, position: Int) {
        val device = list[position]
        holder.lightBrightness.progress = device.brightness
        holder.lightType.text = device.name
        holder.lightId = device.id

        holder.lightSwitch.isChecked = when(device.brightness){
            0 -> false
            else -> true
        }


        holder.lightBrightness.onSeekBarChangeListener {
            onProgressChanged { seekbar: SeekBar?, i: Int, b: Boolean ->
                if (seekbar != null) {
                    holder.lightSwitch.isChecked = when(i){
                        0 -> false
                        else -> true
                    }
                    model.putLamp(this@LightControllerAdapter, holder.lightId, i)
                }
            }
        }

        holder.lightSwitch.setOnCheckedChangeListener { _, isChecked ->
            holder.lightBrightness.progress = when(isChecked) {
            false -> 0
            true -> holder.lightBrightness.max }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LightControllerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lightBrightness: SeekBar
        var lightType: TextView
        var lightId: Int = 0
        var lightSwitch: Switch
        init {
            lightBrightness = itemView.findViewById(LightControllerUI.lightBrightness)
            lightType = itemView.findViewById(LightControllerUI.lightType)
            lightSwitch = itemView.findViewById(LightControllerUI.lightSwitch)
        }
    }

    override fun onPutSuccess(callback: ResponseJSON<LightController>) {
        println("put a lamp!")
    }

    override fun onFailure(networkError: Throwable) {
        println("couldn't put a lamp")
    }
}