package com.konkuk17.messenger_example.Friends

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList


class FriendList(
    val status: String?,
    val message: ArrayList<ShowFriend>?
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(ShowFriend)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeTypedList(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendList> {
        override fun createFromParcel(parcel: Parcel): FriendList {
            return FriendList(parcel)
        }

        override fun newArray(size: Int): Array<FriendList?> {
            return arrayOfNulls(size)
        }
    }
}