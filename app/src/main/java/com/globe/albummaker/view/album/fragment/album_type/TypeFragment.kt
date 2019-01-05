package com.globe.albummaker.view.album.fragment.album_type

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.globe.albummaker.R
import com.globe.albummaker.constants.albumType
import com.globe.albummaker.util.GlideApp
import com.globe.albummaker.view.album.AlbumTemplateSelectActivity
import java.io.File


class TypeFragment : TypeBaseFragment(), View.OnClickListener {

    private lateinit var mView: View
    private var mType = -1
    lateinit var photoList: Array<String>
    private var imageViewCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments!!.getInt("typeInfo")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(albumType[mType], container, false)
        mView = view
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findAllImageViewAndInitArray()
    }

    private fun findAllImageViewAndInitArray() {
        val viewGroup = mView as ViewGroup
        val childCount = viewGroup.childCount
        imageViewCount = childCount
        for (i in 0..childCount) {
            val view = viewGroup.getChildAt(0)
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
        Log.e("TAG", "ddd")
        if (view!!.id == R.id.albumEditFragmentContainer_0) {
            val intent = Intent(context, AlbumTemplateSelectActivity::class.java)
            with(intent) {
                putExtra("isFirstPage", (mType == 1))
                startActivity(this)
            }
        }

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
