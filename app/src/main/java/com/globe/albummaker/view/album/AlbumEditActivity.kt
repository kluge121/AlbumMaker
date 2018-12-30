package com.globe.albummaker.view.album

import android.os.Bundle
import com.globe.albummaker.R
import com.globe.albummaker.view.album.adapter.AlbumEditViewPagerAdapter
import com.globe.albummaker.view.album.fragment.AlbumEditFragment
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.globe.testproject.constants.NEW_ALBUM
import com.globe.testproject.constants.STORED_ALBUM
import com.globe.testproject.data.realm.RealmAlbum
import com.globe.testproject.data.realm.RealmAlbumPageData
import com.globe.testproject.extension.getAlbumNextKey
import com.globe.testproject.extension.getAlbumPageNextKey
import io.realm.Realm

class AlbumEditActivity : StatusTransparentActivity() {

    //바텀 리싸이클러와 메인컨텐츠 뷰페이저 각각의 별도의 순서 동기화 처리 필요


    var mAlbum: RealmAlbum? = null
    lateinit var viewPagerAdapter: AlbumEditViewPagerAdapter

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
                continue
            }
            mAlbum!!.pageDatas.add(pageData)
            viewPagerAdapter.addFragmentPage(AlbumEditFragment.newInstance(pageData))
        }

    }

    private fun initStoredAlbum() {

    }

    fun replaceFragmentContent() {}


}
