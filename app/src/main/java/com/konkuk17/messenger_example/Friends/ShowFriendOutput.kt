package com.konkuk17.messenger_example.Friends

import android.os.Parcel
import android.os.Parcelable


data class ShowFriendOutput(
    var code : String,
    var msg : String,
    var uf_friend_name: String?,
    var uf_friend_id: String?,
    var uf_favorite_state: String?
)