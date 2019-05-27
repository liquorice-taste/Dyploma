package com.carys.dyploma.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
//import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import org.jetbrains.anko.*
import com.carys.dyploma.activities.dataModels.HomeSystem
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.viewPager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.carys.dyploma.activities.dataModels.Room
import com.carys.dyploma.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.drawerLayout

class MainViewActivity: AppCompatActivity()  {
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
                            navigationIcon = ContextCompat.getDrawable(ctx, R.mipmap.home_icon)
                            imageButton {
                                setImageResource(R.mipmap.home_icon)
                                onClick {
                                    presenter.getSystems()
                                }

                            }.lparams(gravity = Gravity.RIGHT)

                        }.lparams(matchParent, wrapContent)

                        lparams { width = matchParent; height = wrapContent; isLiftOnScroll = true }

                        mTabLayout = themedTabLayout() {
                            lparams(matchParent, wrapContent)
                            {
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

                }
            }
        }
        fun messageSystems(list: ArrayList<HomeSystem>) = with(myui) {
            GlobalScope.launch(Dispatchers.Main) {
                selector("Choose your home:", list.map { it.name })
                { _, i -> home = list[i] ; presenter.getRooms() }
            }
        }
    }
}