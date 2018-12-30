package com.globe.testproject.data.realm

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable


open class RealmAlbumPageData() : RealmObject(), Parcelable {
    @PrimaryKey
    open var id: Int = -1
    open var frameType1: Int = 0
    open var frameType2: Int = 0
    open var frameType3: Int = 0
    open var pagePreviewPath: String? = ""
    open var framePhotoList1: RealmList<String> = RealmList()
    open var framePhotoList2: RealmList<String> = RealmList()
    open var isSingle: Boolean = true
    open var sequence: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        frameType1 = parcel.readInt()
        frameType2 = parcel.readInt()
        frameType3 = parcel.readInt()
        pagePreviewPath = parcel.readString()
        isSingle = parcel.readByte() != 0.toByte()
        sequence = parcel.readInt()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(frameType1)
        parcel.writeInt(frameType2)
        parcel.writeInt(frameType3)
        parcel.writeString(pagePreviewPath)
        parcel.writeByte(if (isSingle) 1 else 0)
        parcel.writeInt(sequence)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RealmAlbumPageData> {
        override fun createFromParcel(parcel: Parcel): RealmAlbumPageData {
            return RealmAlbumPageData(parcel)
        }

        override fun newArray(size: Int): Array<RealmAlbumPageData?> {
            return arrayOfNulls(size)
        }
    }


}
