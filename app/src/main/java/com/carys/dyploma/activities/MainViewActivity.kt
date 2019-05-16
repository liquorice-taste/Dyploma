package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carys.dyploma.activities.dataModels.LightController
import com.carys.dyploma.activities.recyclerView.LightControllerAdapter
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.json.JSONObject
import android.content.Intent.getIntent
import android.os.Parcelable
import com.carys.dyploma.activities.dataModels.HomeSystem


class MainViewActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(this)
        MainViewActivityUi(linearLayoutManager).setContentView(this)
        var home = intent.getParcelableExtra("HomeSystem") as HomeSystem
    }

    class MainViewActivityUi(linearLayoutManager: LinearLayoutManager) : AnkoComponent<MainViewActivity> {
        companion object {
            val recycler = 1
        }


        val presenter = MainViewPresenter(this)
        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = 26f
                is EditText -> v.textSize = 24f
                is TextView -> v.textSize = 26f
            }
        }
        lateinit var rec: RecyclerView
        var devs: ArrayList<LightController> = arrayListOf()

        @SuppressLint("SetTextI18n")
        override fun createView(ui: AnkoContext<MainViewActivity>) = with(ui) {
            //rec.setRecyclerListener { devs }
            presenter.func()
            verticalLayout {
                textView() {
                    text = "Lights"
                }

                rec = recyclerView {
                    id = recycler
                    layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
                    adapter = LightControllerAdapter(devs)
                }

                bottomNavigationView {

                }
            }.applyRecursively(customStyle)

        }
    }
}