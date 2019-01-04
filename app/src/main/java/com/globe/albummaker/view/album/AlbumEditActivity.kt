package com.globe.albummaker.view.album

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.globe.albummaker.R
import com.globe.albummaker.view.album.adapter.AlbumEditViewPagerAdapter
import com.globe.albummaker.view.album.fragment.AlbumEditFragment
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.globe.albummaker.constants.NEW_ALBUM
import com.globe.albummaker.constants.STORED_ALBUM
import com.globe.albummaker.data.realm.RealmAlbum
import com.globe.albummaker.data.realm.RealmAlbumPageData
import com.globe.albummaker.extension.getAlbumNextKey
import com.globe.albummaker.extension.getAlbumPageNextKey
import com.globe.albummaker.view.album.adapter.AlbumEditContentRecyclerViewAdapter
import com.globe.albummaker.view.album.adapter.AlbumNumberItemDecoration
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_album_edit.*

class AlbumEditActivity : StatusTransparentActivity() {

    //바텀 리싸이클러와 메인컨텐츠 뷰페이저 각각의 별도의 순서 동기화 처리 필요

    var mAlbum: RealmAlbum? = null
    lateinit var viewPagerAdapter: AlbumEditViewPagerAdapter
    lateinit var recyclerViewAdapter: AlbumEditContentRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_edit)
        setStatusTransparent()
        viewPagerAdapter = AlbumEditViewPagerAdapter(supportFragmentManager)

        initWidget(NEW_ALBUM)
    }

    private fun initWidget(isNewAlbum: Int) {
        if (isNewAlbum == NEW_ALBUM)
            initNewAlbum()
        else if (isNewAlbum == STORED_ALBUM)
            initStoredAlbum()

        album_edit_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                albumEditRecyclerview.scrollToPosition(position)
                recyclerViewAdapter.selectItem(position);
            }
        })
    }

    private fun initNewAlbum() {

        val realm = Realm.getDefaultInstance()
        mAlbum = RealmAlbum()
        mAlbum!!.id = getAlbumNextKey(realm)
        var pageId = getAlbumPageNextKey(realm)
        var sequence = 0

        for (i in 0..12) {
            val pageData = RealmAlbumPageData()
            pageData.id = pageId++
            pageData.sequence = sequence++
            if (i == 1)
                pageData.frameType1 = 1

            if (i == 12) {
                pageData.id = pageData.id * 2
                mAlbum!!.pageDatas.add(pageData)
                break
            }


            mAlbum!!.pageDatas.add(pageData)
            viewPagerAdapter.addFragmentPage(AlbumEditFragment.newInstance(pageData))
        }
        recyclerViewAdapter = AlbumEditContentRecyclerViewAdapter(mAlbum!!,
            object : AlbumEditContentRecyclerViewAdapter.IAlbumEditContentRecyclerListener {
                override fun syncViewPagerPosition(position: Int) {
                    album_edit_viewpager.currentItem = position
                }

            }
        )
        album_edit_viewpager.adapter = viewPagerAdapter
        viewPagerAdapter.notifyDataSetChanged()

        albumEditRecyclerview.adapter = recyclerViewAdapter
        albumEditRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        albumEditRecyclerview.addItemDecoration(
            AlbumNumberItemDecoration(
                this,
                R.dimen.album_side_number_space
            )
        )
        (albumEditRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        realm.close()
    }

    private fun initStoredAlbum() {

    }


}
