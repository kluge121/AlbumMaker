package com.globe.albummaker.view.album.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.data.realm.RealmAlbum
import com.globe.albummaker.data.realm.RealmAlbumPageData
import com.globe.albummaker.extension.getAlbumPageNextKey
import com.globe.albummaker.util.GlideApp
import io.realm.Realm
import kotlinx.android.synthetic.main.recyclerview_album_nomal_list_item.view.*
import java.util.*

class AlbumEditContentRecyclerViewAdapter(var album: RealmAlbum, var listener: IAlbumEditContentRecyclerListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperCallback.ItemTouchHelperListener {


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
                // 이미 선택된 아이템을 누른다면
                if (currentSelectItem == position) {
                    if (position == 0 || position == 1) {
                        Toast.makeText(context, "해당 페이지는 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (list.size == 13) {
                        Toast.makeText(context, "더 이상 페이지를 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    AlertDialog.Builder(context)
                            .setTitle("페이지를 삭제하시겠습니까?")
                            .setNegativeButton("취소") { dialog, i ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("확인") { dialog, i ->
                                val realm = Realm.getDefaultInstance()
                                realm.executeTransaction {
                                    val catchPosition = position
                                    val deleteItem = list[position]
                                    list.remove(deleteItem)
                                    if (deleteItem!!.isManaged) {
                                        deleteItem.deleteFromRealm()
                                    }
                                    listener.removePageViewPagerPage(position)

                                    if (catchPosition == currentSelectItem && currentSelectItem > 0) {
                                        currentSelectItem--
                                        listener.syncViewPagerPosition(currentSelectItem)
                                    }
                                    sequenceReSetting()

                                    notifyDataSetChanged()
                                    realm.close()

                                }

                            }.show()
                } else {
                    selectItem(position)
                    listener.syncViewPagerPosition(position)
                }


                //뷰페이지 페이지 이동
            }
        } else if (holder is AlbumNumberLastListViewHolder) {
            holder.itemView.setOnClickListener {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val newPage = RealmAlbumPageData()
                    newPage.id = getAlbumPageNextKey(realm)
                    newPage.sequence = list.size - 1
                    list.add(list.size - 1, newPage)
                    listener.addPageViewPagerPager(newPage)
                    notifyDataSetChanged()
                }
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

    fun selectItem(position: Int) {
        val tmp = currentSelectItem
        currentSelectItem = position
        notifyItemChanged(tmp)
        notifyItemChanged(currentSelectItem)
    }

    private fun sequenceReSetting() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            var sequence = 0;
            for (page in list) {
                page.sequence = sequence++
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {


        val fromData = list[fromPosition]
        list.remove(fromData)
        list.add(toPosition, fromData)
        notifyItemMoved(fromPosition, toPosition)
        sequenceReSetting()
        return true
    }

    interface IAlbumEditContentRecyclerListener {
        fun syncViewPagerPosition(position: Int)
        fun removePageViewPagerPage(position: Int)
        fun addPageViewPagerPager(pageData: RealmAlbumPageData)
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

    fun reSettingPageNumber() {
        itemView.recyclerview_number_list_left_tv.text = ((adapterPosition - 1) * 2).toString()
        itemView.recyclerview_number_list_right_tv.text = ((adapterPosition - 1) * 2 + 1).toString()
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
