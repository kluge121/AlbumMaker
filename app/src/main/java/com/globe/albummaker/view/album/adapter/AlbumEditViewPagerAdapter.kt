package com.globe.albummaker.view.album.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.globe.albummaker.view.album.fragment.AlbumEditFragment


class AlbumEditViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    var list = ArrayList<AlbumEditFragment>()


    fun addFragmentPage(pageFragment: AlbumEditFragment) {
            list.add(pageFragment)
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }



}
