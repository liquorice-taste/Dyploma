package com.carys.dyploma.activities.recyclerView

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.dataModels.ResponseJSON
import com.carys.dyploma.activities.dataModels.Responset
import com.carys.dyploma.activities.dataModels.Sensor
import kotlinx.android.synthetic.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onSeekBarChangeListener

class LightControllerAdapter(var list: ArrayList<LightController> = arrayListOf()) : LightControllerCallback, RecyclerView.Adapter<LightControllerAdapter.LightControllerViewHolder>() {

    val model = LightControllerModel()

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
        holder.lightType.text = device.type
        holder.lightId = device.id

        holder.lightBrightness.onSeekBarChangeListener {
            onProgressChanged { seekbar: SeekBar?, i: Int, b: Boolean ->
                if (seekbar != null) {
                    //model.putLamp(this@LightControllerAdapter, holder.lightId, i)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LightControllerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lightBrightness: SeekBar
        var lightType: TextView
        var lightId: Int = 0
        init {
            lightBrightness = itemView.findViewById(LightControllerUI.lightBrightness)
            lightType = itemView.findViewById(LightControllerUI.lightType)
        }
    }

    override fun onPutSuccess(callback: ResponseJSON<Responset>) {
        println("put a lamp!")
    }

    override fun onFailure(networkError: Throwable) {
        println("couldn't put a lamp")
    }
}