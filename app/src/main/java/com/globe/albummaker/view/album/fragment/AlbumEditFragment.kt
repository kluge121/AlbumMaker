package com.globe.albummaker.view.album.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.albummaker.R
import com.globe.albummaker.view.album.fragment.album_type.TypeFragment
import com.globe.albummaker.data.realm.RealmAlbumPageData
import com.globe.albummaker.extension.replaceFragment
import kotlinx.android.synthetic.main.fragment_edit_contents.*


class AlbumEditFragment : Fragment(), IAlbumEditFragment {

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

        val leftFragment = TypeFragment.newInstance(0)
        val rightFragment = TypeFragment.newInstance(0)

        replaceFragment(leftFragment, R.id.albumEditFragmentContainer1)
        replaceFragment(rightFragment, R.id.albumEditFragmentContainer2)

    }

    private fun initDoubleSideWidget() {
        viewDoubleMode()
        val doubleSideFragment = TypeFragment.newInstance(mPageInfo.frameType3)
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


}
