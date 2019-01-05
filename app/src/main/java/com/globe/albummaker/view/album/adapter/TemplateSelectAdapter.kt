package com.globe.albummaker.view.album.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.view.album.AlbumTemplateSelectActivity
import kotlinx.android.synthetic.main.recyclerview_album_dual_template_item.view.*
import kotlinx.android.synthetic.main.recyclerview_album_single_template_item.view.*


class TemplateSelectAdapter : RecyclerView.Adapter<TemplateBasicViewHolder> {

    lateinit var context: Context
    private var currentTab = 0
    val singleList: ArrayList<TemplateInfo>
    val dualList: ArrayList<TemplateInfo>
    val SINGLE = 1
    val DUAL = 0

    constructor() {
        singleList = ArrayList()
        dualList = ArrayList()

        dualList.add(TemplateInfo(false, 2, R.drawable.type2))
        singleList.add(TemplateInfo(true, 3, R.drawable.type3))
    }

    fun setCurrentTab(tabPosition: Int) {
        currentTab = tabPosition
    }


    override fun getItemViewType(position: Int): Int {
        return if (currentTab == 0) SINGLE
        else DUAL
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateBasicViewHolder {
        context = parent.context
        if (viewType == SINGLE) {
            val view =
                    LayoutInflater.from(context).inflate(R.layout.recyclerview_album_single_template_item, parent, false)
            return TemplateSelectSingleViewHolder(view)
        } else {
            val view =
                    LayoutInflater.from(context).inflate(R.layout.recyclerview_album_dual_template_item, parent, false)
            return TemplateSelectDualViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: TemplateBasicViewHolder, position: Int) {
        if (currentTab == 0) {
            holder.setView(singleList[position])
            holder.itemView.setOnClickListener {
                val parentActivity = (context as AlbumTemplateSelectActivity)
                parentActivity.selectTemplateAndFinish(singleList[position].type)

            }
        } else if (currentTab == 1) {
            holder.setView(dualList[position])
            holder.itemView.setOnClickListener {
                val parentActivity = (context as AlbumTemplateSelectActivity)
                parentActivity.selectTemplateAndFinish(dualList[position].type)

            }
        }
    }

    override fun getItemCount(): Int {
        return if (currentTab == 0)
            singleList.size
        else
            dualList.size
    }

}


abstract class TemplateBasicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setView(data: TemplateInfo)
}

class TemplateSelectSingleViewHolder(view: View) : TemplateBasicViewHolder(view) {
    val mainView = view
    val image = itemView.recyclerview_template_image!!

    override fun setView(data: TemplateInfo) {
        if (data.imagePath != 0)
            image.setImageResource(data.imagePath)
    }
}

class TemplateSelectDualViewHolder(view: View) : TemplateBasicViewHolder(view) {
    val mainView = view
    val image = itemView.recyclerview_dual_template_image1
    override fun setView(data: TemplateInfo) {
        if (data.imagePath != 0)
            image.setImageResource(data.imagePath)
    }
}

class AlbumTemplateSingleItemDecoration : RecyclerView.ItemDecoration {

    var itemOffset: Int? = null
    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    constructor(itemOffset: Int) {
        this.itemOffset = itemOffset
    }

    constructor(context: Context, @DimenRes itemOffsetId: Int) {
        this.itemOffset = context.resources.getDimensionPixelOffset(itemOffsetId)
        this.context = context
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val sideSpaceOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_side_space)
        val sideBetweenOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_between_space)
        val spaceTopOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_top_space)
        val spaceBetweenOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_between_space)
        // first


        if (position % 2 == 0) {
            outRect.set(sideSpaceOffset, spaceTopOffset, sideBetweenOffset, 0)
        } else {
            outRect.set(sideBetweenOffset, spaceTopOffset, sideSpaceOffset, 0)
        }
    }
}

class AlbumTemplateDualItemDecoration : RecyclerView.ItemDecoration {

    var itemOffset: Int? = null
    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    constructor(itemOffset: Int) {
        this.itemOffset = itemOffset
    }

    constructor(context: Context, @DimenRes itemOffsetId: Int) {
        this.itemOffset = context.resources.getDimensionPixelOffset(itemOffsetId)
        this.context = context
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val sideSpaceOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_side_space)
        val spaceTopOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_top_space)
        val spaceBetweenOffset = context!!.resources.getDimensionPixelOffset(R.dimen.album_template_between_space)
        // first
        outRect.set(spaceTopOffset, spaceTopOffset, spaceTopOffset, 0)
    }
}


data class TemplateInfo(
        var isSingle: Boolean,
        var type: Int,
        var imagePath: Int
)
