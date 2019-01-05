package com.globe.albummaker.view.album.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemTouchHelperCallback(var listener: ItemTouchHelperListener) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlag, 0)

    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

        if (viewHolder.adapterPosition <=  1 || target.adapterPosition <= 1) return false
        if (viewHolder is AlbumNumberNomalListViewHolder && target is AlbumNumberNomalListViewHolder) {
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition())
            viewHolder.reSettingPageNumber()
            target.reSettingPageNumber()
        }

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //non swipe
    }

    interface ItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    }
}
