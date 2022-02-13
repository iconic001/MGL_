package com.mwachakagrouplimited.mglic.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class Menu protected constructor(`in`: Parcel) : Parcelable {
    var name: String?
    var price: Float
    var totalInCart: Int
    var url: String?
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeFloat(price)
        dest.writeString(url)
        dest.writeInt(totalInCart)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Menu?> = object : Creator<Menu?> {
            override fun createFromParcel(`in`: Parcel): Menu? {
                return Menu(`in`)
            }

            override fun newArray(size: Int): Array<Menu?> {
                return arrayOfNulls(size)
            }
        }
    }

    init {
        name = `in`.readString()
        price = `in`.readFloat()
        url = `in`.readString()
        totalInCart = `in`.readInt()
    }
}