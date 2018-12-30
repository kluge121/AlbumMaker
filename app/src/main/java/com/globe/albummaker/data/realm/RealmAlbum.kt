package com.globe.testproject.data.realm

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

open class RealmAlbum() : RealmObject(), Parcelable {
    @PrimaryKey
    open var id: Int = 0
    open var title: String = ""
    open var subTitle: String = ""
    open var contents: String = ""
    open var count: Int = 1
    open var price: Int = 0
    open var coverImage: String = ""
    open var shape: Int = 0
    open var size: Int = 0
    open var coverType: Int = 0
    open var coverCoating: Int = 0
    open var innerType: Int = 0
    open var pageDatas: RealmList<RealmAlbumPageData> = RealmList()

    @Ignore
    open var tmpPrice: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        subTitle = parcel.readString()
        contents = parcel.readString()
        count = parcel.readInt()
        price = parcel.readInt()
        coverImage = parcel.readString()
        shape = parcel.readInt()
        size = parcel.readInt()
        coverType = parcel.readInt()
        innerType = parcel.readInt()
        coverCoating = parcel.readInt()
        pageDatas.addAll(parcel.createTypedArrayList(RealmAlbumPageData.CREATOR))
        tmpPrice = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(contents)
        parcel.writeInt(count)
        parcel.writeInt(price)
        parcel.writeString(coverImage)
        parcel.writeInt(shape)
        parcel.writeInt(size)
        parcel.writeInt(coverType)
        parcel.writeInt(coverCoating)
        parcel.writeInt(innerType)
        parcel.writeTypedList(pageDatas)
        parcel.writeInt(tmpPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RealmAlbum> {
        override fun createFromParcel(parcel: Parcel): RealmAlbum {
            return RealmAlbum(parcel)
        }

        override fun newArray(size: Int): Array<RealmAlbum?> {
            return arrayOfNulls(size)
        }
    }

}
