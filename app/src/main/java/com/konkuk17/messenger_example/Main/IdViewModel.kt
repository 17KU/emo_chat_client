package com.konkuk17.messenger_example.Main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData

class IdViewModel : ViewModel(){
    val selectednum = MutableLiveData<Int>()

    fun setLiveData(num: Int){
        selectednum.value = num
    }

    val myId = MutableLiveData<String>()

    fun setMyId(id : String){
        myId.value = id
    }

    val myName = MutableLiveData<String>()

    fun setMyNmae(name : String){
        myName.value = name
    }

    val friendList = MutableLiveData<ArrayList<FriendRecycleViewData>>()

    fun setFriendList(myFriendList: ArrayList<FriendRecycleViewData>){
        friendList.value = myFriendList
    }

    fun getFriendList() : ArrayList<FriendRecycleViewData>?{
        return friendList.value
    }

}