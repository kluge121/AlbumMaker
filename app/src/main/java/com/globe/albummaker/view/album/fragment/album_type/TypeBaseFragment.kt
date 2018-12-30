package com.globe.albummaker.view.album.fragment.album_type

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.IOException


open class TypeBaseFragment : Fragment() {

    lateinit var imageCallback: IimageSetCallback
    lateinit var currentPhotoPath: String //실제 사진 파일 경로
    private val GALLERY_CODE = 1112


    fun selectGallery() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_CODE)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(context, "사진 등록을 위한 저장 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
        TedPermission.with(context)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("사진 등록을 위하여 저장 권한을 확인해주세요")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }

    fun sendPicture(imgUri: Uri?): String {
        val imagePath = getRealPathFromURI(imgUri) // path 경로
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(imagePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imagePath
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                GALLERY_CODE -> {
                    currentPhotoPath = sendPicture(data!!.data) //갤러리에서 가져오기
                    if (currentPhotoPath != null) {
                        imageCallback.setImage(currentPhotoPath)
                    }
                }
                else -> {
                }
            }

        }
    }


    fun getRealPathFromURI(contentUri: Uri?): String {
        var column_index = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context!!.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(column_index)
    }
}


interface IimageSetCallback {
    fun setImage(uri: String)
}