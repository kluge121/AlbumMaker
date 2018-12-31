package com.globe.albummaker.extension


import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.globe.albummaker.view.album.fragment.AlbumEditFragment
import com.globe.albummaker.data.realm.RealmAlbum
import com.globe.albummaker.data.realm.RealmAlbumPageData
import io.realm.Realm


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun AlbumEditFragment.addFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AlbumEditFragment.replaceFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { replace(frameId, fragment) }
}


fun getAlbumNextKey(realm: Realm): Int {
    try {
        val number = realm.where(RealmAlbum::class.java).max("id")
        if (number != null) {
            var value = number.toInt() * 10 + SystemClock.currentThreadTimeMillis().toInt() + number.toInt() * 100
            return value
        } else {
            return 0
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        return 0
    }

}

fun getAlbumPageNextKey(realm: Realm): Int {
    try {
        val number = realm.where(RealmAlbumPageData::class.java).max("id")
        if (number != null) {
            var value = number.toInt() * 100000 + SystemClock.currentThreadTimeMillis().toInt()
            return value.toInt()
        } else {
            return 0
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        return 0
    }

}