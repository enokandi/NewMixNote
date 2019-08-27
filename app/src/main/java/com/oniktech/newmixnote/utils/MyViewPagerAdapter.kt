package com.oniktech.newmixnote.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyViewPagerAdapter(fm: FragmentManager?): FragmentPagerAdapter(fm!!) {

    val fragments: MutableList<Fragment> = ArrayList();
    val fragmentNames : MutableList<String> = ArrayList()

    fun addFragment(fragment: Fragment , name: String){
        fragments.add(fragment)
        fragmentNames.add(name)
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}