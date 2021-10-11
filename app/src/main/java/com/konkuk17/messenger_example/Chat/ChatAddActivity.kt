package com.konkuk17.messenger_example.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.konkuk17.messenger_example.Friends.FriendRecycleViewAdapter
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatAddBinding

class ChatAddActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatAddBinding
    var selectedFriend : String? = null
    var friendList : ArrayList<FriendRecycleViewData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        friendList= intent.getSerializableExtra("friendList") as ArrayList<FriendRecycleViewData>

        if(friendList != null){
            Log.d("chat add activity", "size : " + friendList?.size)
            for (f in friendList!!){
                Log.d("chat add activity", "name : " + f?.name)
            }
            //binding.chataddRecyclerview.adapter = FriendRecycleViewAdapter(this, friendList, )
        }
    }
}