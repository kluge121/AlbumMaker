package com.globe.albummaker.view.album.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.data.realm.RealmAlbum
import com.globe.albummaker.data.realm.RealmAlbumPageData
import com.globe.albummaker.util.GlideApp
import kotlinx.android.synthetic.main.recyclerview_album_nomal_list_item.view.*

class AlbumEditContentRecyclerViewAdapter(var album: RealmAlbum) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    private val NOAML_ITEM = 1
    private val LAST_ITEM = 2
    private var list = album.pageDatas
    private var currentSelectItem = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        context = parent.context
        return if (viewType == NOAML_ITEM) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_album_nomal_list_item, parent, false)
            AlbumNumberNomalListViewHolder(view)
        } else {
            view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_album_last_list_item, parent, false)
            AlbumNumberLastListViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is AlbumNumberNomalListViewHolder) {
            holder.setView(context, list[position]!!, currentSelectItem == position, position)

            holder.itemView.setOnClickListener {
                val tmp = currentSelectItem
                currentSelectItem = position
                notifyItemChanged(tmp)
                notifyItemChanged(currentSelectItem)

                //뷰페이지 페이지 이동
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == list.size - 1) return LAST_ITEM
        else return NOAML_ITEM
    }

    fun getCurrentItemIndex(): Int {
        return currentSelectItem
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}


class AlbumNumberNomalListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var preview = view.recyclerview_number_list_container3

    fun setView(context: Context, data: RealmAlbumPageData, isSelect: Boolean, position: Int) {

        if (data.pagePreviewPath.isNullOrEmpty()) {
            val drawable: Int
            if (position == 1)
                drawable = R.drawable.second_preview
            else
                drawable = R.drawable.first_preview
            GlideApp.with(preview)
                .load(drawable)
                .into(preview)
        } else {
            GlideApp.with(preview)
                .load(context.getFileStreamPath(data.pagePreviewPath))
                .into(preview)

        }

        if (position == 0) {
            itemView.recyclerview_number_list_left_tv.text = ""
            itemView.recyclerview_number_list_right_tv.text = ""
            itemView.recyclerview_number_list_center_tv.text = "앨범 커버"

        } else if (position == 1) {
            itemView.recyclerview_number_list_left_tv.text = "x"
            itemView.recyclerview_number_list_right_tv.text = "1"
            itemView.recyclerview_number_list_center_tv.text = ""
        } else {
            itemView.recyclerview_number_list_left_tv.text = ((position - 1) * 2).toString()
            itemView.recyclerview_number_list_right_tv.text = ((position - 1) * 2 + 1).toString()
            itemView.recyclerview_number_list_center_tv.text = ""
        }
        if (isSelect) {
            itemView.setBackgroundResource(R.drawable.album_page_select_edge)
        } else {
            itemView.setBackgroundResource(R.drawable.album_page_non_select_edge)
        }


    }
}

class AlbumNumberLastListViewHolder(view: View) : RecyclerView.ViewHolder(view)

class AlbumNumberItemDecoration : RecyclerView.ItemDecoration {

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

        val sideSpaceOffset =
            context!!.resources.getDimensionPixelOffset(R.dimen.album_side_number_space)
        val spaceOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_number_space)
        // first
        if (position == 0) {
            outRect.set(sideSpaceOffset, 0, 0, 0)
            // last
        } else if (itemCount > 0 && position == itemCount - 1) {
            outRect.set(sideSpaceOffset, 0, sideSpaceOffset, 0)

        } else outRect.set(spaceOffset, 0, 0, 0)
    }
}
