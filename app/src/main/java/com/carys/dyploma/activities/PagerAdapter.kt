package com.carys.dyploma.activities

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.carys.dyploma.activities.dataModels.Room
import com.carys.dyploma.activities.recyclerView.SensorUI

class PagerAdapter(val fm: FragmentManager, behaviour: Int) : FragmentPagerAdapter(fm, behaviour) {


    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun addRoomList(rooms: List<Room>) {

        rooms.forEach{
            mFragmentList.add(RoomContentActivity(it.id, it.id) )
            mFragmentTitleList.add(it.name)
        }
        notifyDataSetChanged()
    }


    fun refreshRoomList(rooms: List<Room>) {
        mFragmentList.forEach {
            /*val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.remove(it)
            fragmentTransaction.commit()*/


            //destroyItem(it.view as ViewGroup, it.id, it)
            //fm.popBackStack()
            //mFragmentTitleList.remove()
       }
        mFragmentList.clear()
        mFragmentTitleList.clear()
        //notifyDataSetChanged()
        addRoomList(rooms)
    }
}