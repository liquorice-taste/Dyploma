package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
//import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import org.jetbrains.anko.*
import com.carys.dyploma.dataModels.HomeSystem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.viewPager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.carys.dyploma.dataModels.Room
import com.carys.dyploma.R
import com.carys.dyploma.general.SharedUtils
import com.carys.dyploma.presenters.MainViewPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.drawerLayout
import java.io.IOException

class MainViewActivity: AppCompatActivity()  {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var tb: Toolbar
        appBarLayout {
            tb = Toolbar(context).apply {

                id = R.id.toolbar
            }
        }
        //var toolbar: Toolbar = find(R.id.toolbar)
        setSupportActionBar(tb)
        //toolbar.find<Toolbar>(R.id.toolbar)
        MainViewActivityUI(supportFragmentManager, intent.getParcelableExtra("HomeSystem") as HomeSystem)
            .setContentView(this)
    }


    class MainViewActivityUI(private val fm: FragmentManager, var home: HomeSystem) : AnkoComponent<MainViewActivity> {
        lateinit var mTabLayout: TabLayout
        lateinit var mViewPager: ViewPager
        lateinit var roomList: List<Room>
        lateinit var myui: AnkoContext<MainViewActivity>
        private val presenter = MainViewPresenter(this)

        lateinit var pagerAdapter: PagerAdapter

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("ResourceType")
        override fun createView(ui: AnkoContext<MainViewActivity>) = with(ui) {
            myui = ui
            drawerLayout {
                coordinatorLayout {

                    lparams(matchParent, matchParent)

                    appBarLayout {
                        toolbar {
                            id = R.id.toolbar
                            title = resources.getString(R.string.app_name)
                            menu.apply {
                                add(resources.getText(R.string.settings)).apply {
                                    setIcon(R.drawable.ic_menu_camera)
                                    setOnMenuItemClickListener {
                                        toast("hi")
                                        true
                                    }
                                }
                                add(resources.getText(R.string.change_system)).apply {
                                    setIcon(R.mipmap.home_icon)
                                    setOnMenuItemClickListener {
                                    presenter.getSystems()
                                        true
                                    }
                                }
                                add(resources.getText(R.string.change_user)).apply {
                                    setIcon(R.drawable.ic_menu_camera)
                                    setOnMenuItemClickListener {
                                        launchActivity()
                                        true
                                    }
                                }
                                add(resources.getText(R.string.create_task)).apply {
                                    setIcon(R.drawable.ic_menu_camera)
                                    setOnMenuItemClickListener {
                                        launchActivity()
                                        true
                                    }
                                }
                            }/*
                            navigationIcon = ContextCompat.getDrawable(ctx, R.mipmap.home_icon)
                            imageButton {
                                setImageResource(R.mipmap.home_icon)
                                onClick {
                                    presenter.getSystems()
                                }
`
                            }.lparams(gravity = Gravity.END)
*/
                        }.lparams(matchParent, wrapContent)

                        lparams { width = matchParent; height = wrapContent; isLiftOnScroll = true }

                        mTabLayout = themedTabLayout() {
                            lparams(matchParent, wrapContent) {
                                tabGravity = TabLayout.GRAVITY_FILL
                                tabMode = TabLayout.MODE_FIXED
                            }
                        }
                    }
                    pagerAdapter = PagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
                    presenter.getRooms()
                    mViewPager = viewPager {
                        id = 10
                        adapter = pagerAdapter
                    }.lparams(matchParent, matchParent)
                    mTabLayout.setupWithViewPager(mViewPager)
                    (mViewPager.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                        AppBarLayout.ScrollingViewBehavior()

                    floatingActionButton {
                        image = resources.getDrawable(R.drawable.ic_menu_send)
                        onClick {
                            val mediaRecorder = MediaRecorder()
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                            try {
                                mediaRecorder.prepare()
                                mediaRecorder.start()

                            } catch (e: IOException) {
                                toast(e.localizedMessage)
                            }

                        }
                    }.lparams {
                        margin = dip(resources.getDimension(R.dimen.floating_margin))
                        width = wrapContent
                        height = wrapContent
                        gravity = Gravity.BOTTOM + Gravity.END
                    }

                }
            }
        }
        private fun launchActivity() = with(myui) {
            SharedUtils.remove("Token")
            val intent = Intent(ctx, AuthActivity::class.java)
            ctx.startActivity(intent)
            (ctx as Activity).finish()
        }
        fun messageSystems(list: ArrayList<HomeSystem>) = with(myui) {
            GlobalScope.launch(Dispatchers.Main) {
                selector(resources.getText(R.string.system_selector), list.map { it.name })
                { _, i -> home = list[i] ; presenter.getRooms() }
            }
        }
    }
}