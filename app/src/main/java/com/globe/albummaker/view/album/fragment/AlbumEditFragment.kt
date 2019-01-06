package com.globe.albummaker.view.album.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.albummaker.R
import com.globe.albummaker.constants.DOUBLE_PAGE
import com.globe.albummaker.constants.LEFT_PAGE
import com.globe.albummaker.constants.RIGHT_PAGE
import com.globe.albummaker.data.realm.RealmAlbumPageData
import com.globe.albummaker.extension.replaceFragment
import com.globe.albummaker.view.album.fragment.album_type.TypeFragment
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_edit_contents.*


class AlbumEditFragment : Fragment(), IAlbumEditFragment, TypeFragment.IImageSettingListener {
    //앨범페이지 공간만 제공

    lateinit var mPageInfo: RealmAlbumPageData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPageInfo = arguments!!.getParcelable("info")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_contents, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWidget()
    }

    private fun initWidget() {
        if (mPageInfo.isSingle)
            initSingleSideWidget()
        else
            initDoubleSideWidget()


    }

    private fun initSingleSideWidget() {
        viewSingleMode()

        val leftFragment = TypeFragment.newInstance(mPageInfo.frameType1, LEFT_PAGE)
        val rightFragment = TypeFragment.newInstance(mPageInfo.frameType2, RIGHT_PAGE)

        replaceFragment(leftFragment, R.id.albumEditFragmentContainer1)
        replaceFragment(rightFragment, R.id.albumEditFragmentContainer2)

    }

    private fun initDoubleSideWidget() {
        viewDoubleMode()
        val doubleSideFragment = TypeFragment.newInstance(mPageInfo.frameType3, DOUBLE_PAGE)
        replaceFragment(doubleSideFragment, R.id.albumEditFragmentContainer3)
    }

    private fun viewSingleMode() {
        albumEditFragmentReset1.visibility = View.VISIBLE
        albumEditFragmentReset2.visibility = View.VISIBLE
        albumEditFragmentReset3.visibility = View.INVISIBLE
        albumEditFragmentContainer1.visibility = View.VISIBLE
        albumEditFragmentContainer2.visibility = View.VISIBLE
        albumEditFragmentContainer3.visibility = View.INVISIBLE
    }

    private fun viewDoubleMode() {
        albumEditFragmentReset1.visibility = View.INVISIBLE
        albumEditFragmentReset2.visibility = View.INVISIBLE
        albumEditFragmentReset3.visibility = View.VISIBLE
        albumEditFragmentContainer1.visibility = View.INVISIBLE
        albumEditFragmentContainer2.visibility = View.INVISIBLE
        albumEditFragmentContainer3.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(pageInfo: RealmAlbumPageData): AlbumEditFragment {
            val fragment = AlbumEditFragment()
            val bundle = Bundle()
            bundle.putParcelable("info", pageInfo)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val returnType = data!!.getIntExtra("type", -1)
        val isSingle = data.getBooleanExtra("isSingle", true)
        val position = data.getIntExtra("position", 0)

        mPageInfo.isSingle = isSingle
        val fragment = TypeFragment.newInstance(returnType, position)

        if (isSingle) {
            viewSingleMode()
            if (position == LEFT_PAGE) {
                mPageInfo.frameType1 = returnType
                replaceFragment(fragment, R.id.albumEditFragmentContainer1)
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    mPageInfo.framePhotoList1 = RealmList()
                    for (i in 0..fragment.imageViewCount)
                        mPageInfo.framePhotoList1[i] = ""
                }
                realm.close()

            } else if (position == RIGHT_PAGE) {
                mPageInfo.frameType2 = returnType
                replaceFragment(fragment, R.id.albumEditFragmentContainer2)
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    mPageInfo.framePhotoList2 = RealmList()
                    for (i in 0..fragment.imageViewCount)
                        mPageInfo.framePhotoList1[i] = ""

                }
                realm.close()
            }
        } else {
            viewDoubleMode()
            mPageInfo.frameType3 = DOUBLE_PAGE
            replaceFragment(fragment, R.id.albumEditFragmentContainer3)
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                mPageInfo.framePhotoList1 = RealmList()
                for (i in 0..fragment.imageViewCount)
                    mPageInfo.framePhotoList1[i] = ""

            }
            realm.close()
        }
    }

    override fun imagePathSave(pagePosition: Int, arrayPosition: Int, path: String) {
        if (pagePosition == LEFT_PAGE || pagePosition == DOUBLE_PAGE)
            mPageInfo.framePhotoList1[arrayPosition] = path
        else if (pagePosition == RIGHT_PAGE)
            mPageInfo.framePhotoList2[arrayPosition] = path
    }
}
