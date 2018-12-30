package com.globe.albummaker.view.album.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.testproject.constants.coverCoatingOption
import com.globe.testproject.constants.coverOption
import com.globe.testproject.constants.innerOption
import com.globe.testproject.constants.sizeOption
import com.globe.testproject.data.realm.RealmAlbum
import kotlinx.android.synthetic.main.recyclerview_album_order_item.view.*
import java.text.DecimalFormat

class AlbumOrderListAdapter(val context: Context, albumList: ArrayList<RealmAlbum>) :
    RecyclerView.Adapter<OrderAlbumViewHolder>() {

    var list = albumList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAlbumViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_album_order_item, parent, false)
        return OrderAlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderAlbumViewHolder, position: Int) {
        holder.setView(list[position])
    }

    override fun getItemCount(): Int {
        Log.e("체크", "${list.size}개")
        return list.size
    }

}


class OrderAlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val title = view.recyclerview_album_order_title
    val subTitle = view.recyclerview_album_order_sub_title
    val content = view.recyclerview_album_order_contents
    val content2 = view.recyclerview_album_order_contents2
    val count = view.recyclerview_album_order_count_tv

    val price1 = view.recyclerview_album_order_price1
    val price2 = view.recyclerview_album_order_price2
    val price3 = view.recyclerview_album_order_price3


    fun setView(data: RealmAlbum) {
        val decimalFormat = DecimalFormat("#,###")
        title.text = data.title
        subTitle.text = "사이즈 : ${sizeOption[data.size]} (in)"
        content.text =
                "커버 :${coverCoatingOption[data.coverCoating]} ${coverOption[data.coverType]} / 내지 : ${innerOption[data.innerType]} "
        content2.text = "${(data.pageDatas.size - 3) * 2 + 1}페이지"
        count.text = "수량 : ${data.count}권"
        price1.text = "${decimalFormat.format((data.price) * data.count)}원"
        price2.text = "0원"
        price3.text = "${decimalFormat.format((data.price) * data.count)}원"
    }

}