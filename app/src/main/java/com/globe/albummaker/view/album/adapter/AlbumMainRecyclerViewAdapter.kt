package com.globe.albummaker.view.album.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.testproject.constants.*
import com.globe.testproject.data.realm.RealmAlbum
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.recyclerview_album_item.view.*
import java.text.DecimalFormat


class AlbumMainRecyclerViewAdapter(var listener: IMainActivityListener, albumList: RealmList<RealmAlbum>) :
    RecyclerView.Adapter<AlbumMainRecyclerViewHolder>() {

    var list = albumList
    var count = 0
    lateinit var context: Context


    fun updateAllAlbumList(list: RealmList<RealmAlbum>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun isOrderList(): Boolean {
        return (list.size > 0)
    }

    fun getOrderList(): ArrayList<RealmAlbum> {

        val orderList = ArrayList<RealmAlbum>()
        for (album in list) {
            if (album.count >= 1) {
                orderList.add(album)
            }
        }
        return orderList
    }

    fun getAllAlbumPrice(): Int {
        var result = 0
        for (item in list) {
            result += (item.price * item.count)
        }
        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumMainRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_album_item, parent, false)
        context = parent.context
        return AlbumMainRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (list.size == 0)
            listener.nonItemUpdateView(true)
        else
            listener.nonItemUpdateView(false)
        return list.size
    }

    override fun onBindViewHolder(holder: AlbumMainRecyclerViewHolder, position: Int) {
        holder.setView(list[position]!!)
        holder.itemView.setOnClickListener {
            listener.itemStartActivity(STORED_ALBUM, list[position]!!.id)
        }

        holder.deleteBtn.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("앨범을 삭제하시겠습니까?")
                .setNegativeButton("취소") { p0, p1 ->
                    p0.dismiss()
                }
                .setPositiveButton("확인") { p0, p1 ->
                    //삭제 루틴
                    val realm = Realm.getDefaultInstance()

                    realm.executeTransaction {
                        val pageList = list[position]!!.pageDatas
                        //1. 해당 앨범의 미리보기 스냅샷 지옥보내기
                        for (page in pageList) {
                            val preView = context.getFileStreamPath(page.pagePreviewPath)
                            if (preView.exists() && preView.delete()) {
                                Log.e("삭제확인 성공", preView.absolutePath)
                            } else {
                                Log.e("삭제확인 실패", preView.absolutePath)
                            }
                        }
                        //2. 해당 앨범페이지 realm에서 지옥으로 가버려
                        pageList.deleteAllFromRealm()
                        //3. 해당 앨범 realm에서 지옥으로 가버려
                        val item = list[position]
                        list.remove(item)
                        item!!.deleteFromRealm()
                        notifyDataSetChanged()
                        realm.close()
                    }
                }.show()
        }
    }

    interface IMainActivityListener {
        fun nonItemUpdateView(isNonItem: Boolean)
        fun listUpdate()
        fun getAlbumInfo(): RealmList<RealmAlbum>
        fun itemStartActivity(type: Int, id: Int)
        fun showSnackBar(isShowing: Boolean)
    }


}


class AlbumMainRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val title = view.recyclerview_album_item_title
    val subTitle = view.recyclerview_album_item_sub_title
    val content = view.recyclerview_album_item_contents
    val content2 = view.recyclerview_album_item_contents2
    val count = view.recyclerview_album_item_count
    val price = view.recyclerview_album_item_price
    val plusBtn = view.recyclerview_album_item_plus_btn
    val minusBtn = view.recyclerview_album_item_minus_btn
    val deleteBtn = view.recyclerview_album_item_delete_btn

    fun setView(data: RealmAlbum) {
        priceUpdate(data)
        val decimalFormat = DecimalFormat("#,###")
        title.text = data.title
        subTitle.text = "사이즈 : ${sizeOption[data.size]} (in)"
        content.text =
                "커버 :${coverCoatingOption[data.coverCoating]} ${coverOption[data.coverType]} / 내지 : ${innerOption[data.innerType]} "
        content2.text = "${(data.pageDatas.size - 3) * 2 + 1}페이지"
        count.text = data.count.toString()
        price.text = "${decimalFormat.format((data.price) * count.text.toString().toInt())}"
    }

    fun priceUpdate(data: RealmAlbum) {
        val tmpPrice = (data.price) * count.text.toString().toInt()
        data.tmpPrice = tmpPrice
        val decimalFormat = DecimalFormat("#,###")
        price.text = "${decimalFormat.format(tmpPrice)}"
    }

}

class AlbumItemDecoration : RecyclerView.ItemDecoration {

    var itemOffset: Int? = null
    var context: Context? = null

    constructor(itemOffset: Int) {
        this.itemOffset = itemOffset
    }

    constructor(context: Context, @DimenRes itemOffsetId: Int) {
        this.itemOffset = context.resources.getDimensionPixelOffset(itemOffsetId)
        this.context = context
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount


        val betweenSpaceOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_between_space)
        val firstSpace = context!!.resources.getDimensionPixelOffset(R.dimen.album_first_item_top_space)

        // first
        if (position == 0) {
            outRect.set(0, firstSpace, 0, betweenSpaceOffset)
            //last
        } else if (itemCount > 0 && position == itemCount - 1) {
            outRect.set(0, 0, 0, 0)

        } else outRect.set(0, 0, 0, betweenSpaceOffset)
    }
}