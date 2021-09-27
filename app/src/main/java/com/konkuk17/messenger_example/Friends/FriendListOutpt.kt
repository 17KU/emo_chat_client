package com.konkuk17.messenger_example.Friends

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList


class FriendListOutpt(
    val status: String?,
    val message: ArrayList<ShowFriendOutput>?
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(ShowFriendOutput)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeTypedList(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendListOutpt> {
        override fun createFromParcel(parcel: Parcel): FriendListOutpt {
            return FriendListOutpt(parcel)
        }

        override fun newArray(size: Int): Array<FriendListOutpt?> {
            return arrayOfNulls(size)
        }
    }
}