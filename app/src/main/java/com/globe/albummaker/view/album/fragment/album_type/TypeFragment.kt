package com.globe.albummaker.view.album.fragment.album_type

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.globe.albummaker.R
import com.globe.albummaker.constants.TEMPLATE_SELECT
import com.globe.albummaker.constants.albumType
import com.globe.albummaker.util.GlideApp
import com.globe.albummaker.view.album.AlbumEditActivity
import com.globe.albummaker.view.album.AlbumTemplateSelectActivity
import com.globe.albummaker.view.album.fragment.AlbumEditFragment
import java.io.File


class TypeFragment : TypeBaseFragment(), View.OnClickListener {

    private lateinit var mView: View
    private var mType = -1
    private var mPosition = 0;
    private lateinit var mListener: IImageSettingListener
    var imageViewCount = 0;

    companion object {
        fun newInstance(type: Int, position: Int): TypeFragment {
            val fragment = TypeFragment()
            val bundle = Bundle()
            bundle.putInt("typeInfo", type)
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AlbumEditActivity) {
            mListener =
                    context.supportFragmentManager.findFragmentById(R.id.album_edit_viewpager) as IImageSettingListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments!!.getInt("typeInfo")
        mPosition = arguments!!.getInt("position")

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
        var imageCount = 0;
        for (i in 0..childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is ImageView) {
                view.setOnClickListener(this)
                view.setTag(R.id.imageViewTag, imageCount++)
            }
            if (view is FrameLayout) {
                view.setOnClickListener(this)
            }
        }
        imageViewCount = imageCount

    }


    override fun onClick(view: View?) {

        if (view!!.id == R.id.albumEditFragmentContainer_0) {
            val intent = Intent(context, AlbumTemplateSelectActivity::class.java)
            with(intent) {
                //첫번째 페이지에서 양면페이지 리스트를 안 보여주기 위해서 넘겨줌
                putExtra("isFirstPage", (mType == 1))
                putExtra("position", mPosition)
                parentFragment!!.startActivityForResult(this, TEMPLATE_SELECT)
            }
        }

        if (view is ImageView) {
            imageCallback = object : IimageSetCallback {
                override fun setImage(uri: String) {
                    GlideApp.with(context!!)
                        .load(File(uri))
                        .into(view)
                    (parentFragment as AlbumEditFragment).imagePathSave(
                        mPosition,
                        view.getTag(R.id.imageViewTag) as Int,
                        uri
                    )
                }
            }
            selectGallery()
        }
    }


    interface IImageSettingListener {
        fun imagePathSave(pagePosition: Int, arrayPosition: Int, path: String)
    }

}
