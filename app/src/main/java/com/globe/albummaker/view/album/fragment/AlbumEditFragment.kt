package com.globe.albummaker.view.album.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.albummaker.R
import com.globe.testproject.data.realm.RealmAlbumPageData


class AlbumEditFragment : Fragment(), IAlbumEditFragment {

    lateinit var mPageInfo: RealmAlbumPageData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPageInfo = arguments!!.getParcelable("info")
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_contents, container, false)
        with(view) {
            if(mPageInfo.isSingle){
                //단면 페이지 일 떄


            }else{
                //양면 페이지 일 떄
                //야 알바하는중?? 나 둔산동인데 좀 따 집가는 길에 태평소가서 밥먹을껀데
                //너 알바 끝나고 밥 안먹었으면 좀 따 같이 먹자.
                //


            }

        }
        return view
    }




    fun initWidget() {


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
