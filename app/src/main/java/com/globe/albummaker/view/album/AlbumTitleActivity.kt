package com.globe.albummaker.view.album

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.globe.albummaker.R
import com.globe.albummaker.view.base.StatusTransparentActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_album_title.*
import com.globe.albummaker.custom.CustomSnackbar

class AlbumTitleActivity : StatusTransparentActivity() {

    var snackBar: CustomSnackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_title)
        setStatusTransparent()
        albumTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    hideSnackBar()
                } else {
                    showSnackBar()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        albumTitleBackBtn.setOnClickListener {
            finish()
        }


    }


    private fun showSnackBar() {
        if (snackBar == null) {
            snackBar = CustomSnackbar.make(this.findViewById(android.R.id.content), Snackbar.LENGTH_INDEFINITE,1)
        }
        snackBar!!.setAction("", View.OnClickListener {

            val title = albumTitleEditText.text.toString()
//            val intent = Intent(this, AlbumOptionSelectActivity::class.java)
            intent.putExtra("title",title)
            startActivity(intent)
            finish()


        })
        snackBar!!.setText("확인")
        snackBar!!.show()
    }

    private fun hideSnackBar() {
        if (snackBar != null) {
            snackBar!!.dismiss()

        }
    }

}
