package com.konkuk17.messenger_example.Friends

import java.io.Serializable


data class FriendRecycleViewData (

    var name: String,
    var id: String,
    var favorite: String

        ) : Serializable