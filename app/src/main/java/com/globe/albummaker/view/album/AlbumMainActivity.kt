package com.globe.albummaker.view.album

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.globe.albummaker.R
import com.globe.albummaker.view.album.adapter.AlbumItemDecoration
import com.globe.albummaker.view.album.adapter.AlbumMainRecyclerViewAdapter
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.globe.testproject.data.realm.RealmAlbum
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_album_main.*
import layout.CustomSnackbar
import java.text.DecimalFormat


class AlbumMainActivity : StatusTransparentActivity(), AlbumMainRecyclerViewAdapter.IMainActivityListener {


    var snackBar: CustomSnackbar? = null
    lateinit var adapter: AlbumMainRecyclerViewAdapter
    lateinit var albumRealmList: RealmList<RealmAlbum>
    var marginFlag = false


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_album_main)
        setStatusTransparent()

    }

    private fun newAlbumCreate() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                //TODO 앨범만들러가자
                val intent = Intent(this@AlbumMainActivity, AlbumTitleActivity::class.java)
                startActivity(intent)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@AlbumMainActivity, "사진 등록을 위한 저장 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        TedPermission.with(this@AlbumMainActivity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("사진 등록을 위하여 저장 권한을 확인해주세요")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }


    private fun setWidget() {
        adapter = AlbumMainRecyclerViewAdapter(this, getAlbumInfo())
        albumMainRecyclerView.adapter = adapter
        albumMainRecyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        albumMainRecyclerView.addItemDecoration(AlbumItemDecoration(this, R.dimen.album_between_space))

        albumMainAddBtn.setOnClickListener {
            newAlbumCreate()
        }
        albumMainDefault.setOnClickListener {
            newAlbumCreate()
        }

    }


    override fun nonItemUpdateView(isNonItem: Boolean) {
        if (isNonItem) {
            albumMainDefault.visibility = View.VISIBLE
            albumMainRecyclerView.visibility = View.INVISIBLE
        } else {
            albumMainDefault.visibility = View.INVISIBLE
            albumMainRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun listUpdate() {
        val realm = Realm.getDefaultInstance()
        val albumRealmResult = realm.where(RealmAlbum::class.java).findAll()
        albumRealmList = RealmList()
        albumRealmList.addAll(albumRealmResult.subList(0, albumRealmResult.size))
        adapter.updateAllAlbumList(albumRealmList)
    }

    override fun getAlbumInfo(): RealmList<RealmAlbum> {
        //UI스레드
        val realm = Realm.getDefaultInstance()
        val albumRealmResult = realm.where(RealmAlbum::class.java).findAll()
        albumRealmList = RealmList()
        albumRealmList.addAll(albumRealmResult.subList(0, albumRealmResult.size))
        return albumRealmList
    }

    override fun itemStartActivity(type: Int, id: Int) {
        intent = Intent(this, AlbumEditActivity::class.java).apply {
            putExtra("type", type)
            putExtra("id", id)
            startActivity(this)
        }
    }

    override fun showSnackBar(isShowing: Boolean) {
        if (snackBar == null) {
            snackBar = CustomSnackbar.make(albumMainCoordiatorLayout, Snackbar.LENGTH_INDEFINITE, 0)
        }


        if (adapter.isOrderList()) {
            val decimalFormat = DecimalFormat("#,###")
            snackBar!!.setText(
                "주문하기 ${decimalFormat.format(adapter.getAllAlbumPrice())}원"
            )
            if (!snackBar!!.isShown) {
                snackBar!!.setAction("", View.OnClickListener {
                    if (marginFlag) {
                        albumMainConstraint.setPadding(0, 0, 0, 0)
                        marginFlag = false
                    }
                    val intent = Intent(this@AlbumMainActivity, AlbumOrderActivity::class.java)
                        .apply {
                            putExtra(
                                "orderList",
                                adapter.getOrderList()
                            )
                            putExtra(
                                "amountPrice",
                                adapter.getAllAlbumPrice()
                            )
                            startActivity(this)
                        }
                })
                snackBar!!.show()

                if (!marginFlag) {
                    albumMainConstraint.setPadding(
                        0,
                        0,
                        0,
                        this.resources.getDimensionPixelOffset(R.dimen.snack_bar_height)
                    )
                    marginFlag = true
                }

            }
        } else {
            hideSnackBar()
        }
    }


    private fun hideSnackBar() {
        if (snackBar != null && snackBar!!.isShown) {
            marginFlag = false
            albumMainConstraint.setPadding(0, 0, 0, 0)
            snackBar!!.dismiss()
        }
    }


}
