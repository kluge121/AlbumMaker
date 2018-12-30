package com.globe.albummaker.view.album.fragment.album_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.globe.testproject.constants.albumType


class TypeFragment : TypeBaseFragment(), View.OnClickListener {

    var mType = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments!!.getInt("typeInfo")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(albumType[mType], container, false)
        findAllImageView()
        return view
    }


    private fun findAllImageView() {
        val viewGroup = (view as ViewGroup)
        val childCount = viewGroup.childCount
        for (i in 0..childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is ImageView)
                view.setOnClickListener(this)
        }
    }


    companion object {
        fun newInstance(type: Int): TypeFragment {
            val fragment = TypeFragment()
            val bundle = Bundle()
            bundle.putInt("typeInfo", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(view: View?) {

        if (view is ImageView) {
            imageCallback = object : IimageSetCallback {
                override fun setImage(uri: String) {



                }
            }

            selectGallery()
        }
    }


}
