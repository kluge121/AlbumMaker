package com.globe.albummaker.view.album.fragment.album_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.globe.albummaker.constants.albumType
import com.globe.albummaker.util.GlideApp
import java.io.File


class TypeFragment : TypeBaseFragment(), View.OnClickListener {

    var mType = -1
    var position = 0
    lateinit var photoList: Array<String>
    var imageViewCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments!!.getInt("typeInfo")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(albumType[mType], container, false)
        findAllImageViewAndInitArray()
        return view
    }


    private fun findAllImageViewAndInitArray() {
        val viewGroup = (view as ViewGroup)
        val childCount = viewGroup.childCount
        imageViewCount = childCount
        for (i in 0..childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is ImageView)
                view.setOnClickListener(this)
        }

        photoList = Array(childCount) { "" }
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

                    val parentView = getView() as ViewGroup
                    val childCount = parentView.childCount
                    for (i in 0..childCount) {
                        if (view.id == parentView.getChildAt(i).id) {
                            GlideApp.with(context!!)
                                .load(File(uri))
                                .into(view)

                            //어댑터에서 타입, 뷰 싱크 액티비티 계층에
                            //프리뷰 업데이트


                        }
                    }


                }
            }

            selectGallery()
        }
    }


}
