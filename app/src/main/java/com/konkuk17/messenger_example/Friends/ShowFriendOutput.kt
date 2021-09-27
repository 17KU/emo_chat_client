package com.konkuk17.messenger_example.Friends

import android.os.Parcel
import android.os.Parcelable


data class ShowFriendOutput(
    var id: String?,
    var uf_user_id_id: String?,
    var uf_friend_id: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(uf_user_id_id)
        parcel.writeString(uf_friend_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowFriendOutput> {
        override fun createFromParcel(parcel: Parcel): ShowFriendOutput {
            return ShowFriendOutput(parcel)
        }

        override fun newArray(size: Int): Array<ShowFriendOutput?> {
            return arrayOfNulls(size)
        }
    }
}