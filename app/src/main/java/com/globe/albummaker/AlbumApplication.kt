package com.globe.albummaker

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class AlbumApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("album_test").build()
        Realm.setDefaultConfiguration(config)
    }
}
