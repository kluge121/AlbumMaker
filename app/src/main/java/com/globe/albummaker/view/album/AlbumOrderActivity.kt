package com.globe.albummaker.view.album

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.view.album.adapter.AlbumOrderListAdapter
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.globe.testproject.data.realm.RealmAlbum
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_album_order.*
import kotlinx.android.synthetic.main.layout_order_album_content.*
import layout.CustomSnackbar
import java.text.DecimalFormat

class AlbumOrderActivity : StatusTransparentActivity() {

    lateinit var adapter: AlbumOrderListAdapter
    var snackBar: CustomSnackbar? = null
    var amountPrice: Int = 0
    var disCountPrice: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_order)
        setStatusTransparent()

        @Suppress("UNCHECKED_CAST")
        val orderList = intent.getSerializableExtra("orderList") as ArrayList<RealmAlbum>
        amountPrice = intent.getIntExtra("amountPrice", 0)

        val decimalFormat = DecimalFormat("#,###")

        albumOrderPrice1.text = "${decimalFormat.format(amountPrice)}원"
        albumOrderPrice2.text = "0원"
        albumOrderPrice3.text = "2,500원"
        albumOrderPrice4.text = " ${decimalFormat.format(amountPrice + 2500)}원"

        albumOrderBackBtn.setOnClickListener {
            finish()
        }

        adapter = AlbumOrderListAdapter(this, orderList)
        albumOrderRecyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        albumOrderRecyclerview.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        showSnackBar()
    }

    fun showSnackBar() {
        if (snackBar == null) {
            snackBar = CustomSnackbar.make(this.findViewById(android.R.id.content), Snackbar.LENGTH_INDEFINITE, 1)
//            snackBar = CustomSnackbar.make(this.findViewById(android.R.id.content), Snackbar.LENGTH_INDEFINITE)
            albumOrderConstLayout.setPadding(0, 0, 0, snackBar!!.view.height)
        }


        snackBar!!.setText("배송정보입력")
        if (!snackBar!!.isShown) {
            snackBar!!.setAction("", View.OnClickListener {

                Intent(this@AlbumOrderActivity, AlbumOrderAddressActivity::class.java).apply {
                    putExtra("price1", amountPrice)
                    putExtra("price2", disCountPrice)
                    startActivity(this)
                }
            })

            snackBar!!.show()
        }

    }
}
