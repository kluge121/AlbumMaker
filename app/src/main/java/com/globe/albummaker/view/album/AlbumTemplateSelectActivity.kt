package com.globe.albummaker.view.album

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.view.album.adapter.AlbumTemplateDualItemDecoration
import com.globe.albummaker.view.album.adapter.AlbumTemplateSingleItemDecoration
import com.globe.albummaker.view.album.adapter.TemplateSelectAdapter
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.globe.albummaker.constants.TEMPLATE_SELECT
import kotlinx.android.synthetic.main.activity_album_template_select.*


class AlbumTemplateSelectActivity : StatusTransparentActivity() {
    private var position: Int? = null
    private var isFirstPage: Boolean? = null
    private var tabFlag: Boolean = true
    private val singleDeco = AlbumTemplateSingleItemDecoration(this)
    private val dualDeco = AlbumTemplateDualItemDecoration(this)

    lateinit var adapter: TemplateSelectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_template_select)
        setStatusTransparent()
        initWidget()
        position = intent.getIntExtra("position", -1)
        isFirstPage = intent.getBooleanExtra("isFirstPage", false)
    }

    private fun initWidget() {
        adapter = TemplateSelectAdapter()
        album_template_select_recyclerview.adapter = adapter
        album_template_select_recyclerview.layoutManager = GridLayoutManager(this, 2)
        album_template_select_recyclerview.addItemDecoration(singleDeco)
        albumTemplateSelectBackBtn.setOnClickListener {
            finish()
        }

        albumTemplateSelectSingleBtn.setOnClickListener {
            if (!tabFlag) {
                album_template_select_recyclerview.removeItemDecoration(dualDeco)
                albumTemplateSelectSingleBtn.setTextColor(ContextCompat.getColor(this, R.color.black))
                albumTemplateSelectDualBtn.setTextColor(ContextCompat.getColor(this, R.color.b7b7))
                album_template_select_recyclerview.layoutManager = GridLayoutManager(this, 2)
                album_template_select_recyclerview.addItemDecoration(singleDeco)
                tabFlag = !tabFlag
                adapter.setCurrentTab(0)
                adapter.notifyDataSetChanged()
            }
        }
        albumTemplateSelectDualBtn.setOnClickListener {
            if (tabFlag && !isFirstPage!!) {
                album_template_select_recyclerview.removeItemDecoration(singleDeco)
                albumTemplateSelectSingleBtn.setTextColor(ContextCompat.getColor(this, R.color.b7b7))
                albumTemplateSelectDualBtn.setTextColor(ContextCompat.getColor(this, R.color.black))
                album_template_select_recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                album_template_select_recyclerview.addItemDecoration(dualDeco)
                tabFlag = !tabFlag
                adapter.setCurrentTab(1)
                adapter.notifyDataSetChanged()
            }
            if (isFirstPage!!) {
                Toast.makeText(this, "해당 페이지에는 양면 페이지를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun selectTemplateAndFinish(type: Int) {
        val intent = Intent()
        intent.putExtra("type", type)
        intent.putExtra("position", position)
        setResult(TEMPLATE_SELECT, intent)
        finish()
    }



}