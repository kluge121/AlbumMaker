package com.globe.testproject.extension


import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.globe.testproject.data.realm.RealmAlbum
import com.globe.testproject.data.realm.RealmAlbumPageData
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