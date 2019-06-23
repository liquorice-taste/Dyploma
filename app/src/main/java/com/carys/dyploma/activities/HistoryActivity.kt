package com.carys.dyploma.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.carys.dyploma.presenters.AuthPresenter
import com.carys.dyploma.presenters.HistoryPresenter
import org.jetbrains.anko.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HistoryActivityUI().setContentView(this)
    }

    class HistoryActivityUI : AnkoComponent<HistoryActivity> {

        private val presenter = HistoryPresenter(this)
        lateinit var myui: AnkoContext<HistoryActivity>
        override fun createView(ui: AnkoContext<HistoryActivity>): View  = with(ui) {
            myui = ui
            loadJobs()
            verticalLayout{

            }
        }

        private fun loadJobs() {
            presenter.getJobs()
        }


        fun toast(msg: String?) = with(myui) {
            if (msg != null) {
                ctx.toast(msg)
            }
        }
    }
}