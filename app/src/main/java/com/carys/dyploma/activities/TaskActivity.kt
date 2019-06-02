package com.carys.dyploma.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.carys.dyploma.dataModels.Room
import com.carys.dyploma.presenters.TaskPresenter
import org.jetbrains.anko.*


class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TaskActivityUI(intent.getParcelableExtra("Room") as Room).setContentView(this)
    }

    class TaskActivityUI(val room: Room) : AnkoComponent<TaskActivity> {
        private val presenter = TaskPresenter(this)

        lateinit var myui: AnkoContext<TaskActivity>
        private val customStyle = { v: Any ->
        }

        override fun createView(ui: AnkoContext<TaskActivity>): View = with(ui) {
            linearLayout {


            }
        }

    }
}

