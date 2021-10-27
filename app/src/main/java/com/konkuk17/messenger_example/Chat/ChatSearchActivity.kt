package com.konkuk17.messenger_example.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatAddBinding
import com.konkuk17.messenger_example.databinding.ActivityChatSearchBinding
import com.konkuk17.messenger_example.databinding.FriendrecycleItemInChattingBinding

class ChatSearchActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatSearchBinding

    var chattingList: ArrayList<Chatting>? = null
    var userId: String? = null
    lateinit var chatAdapter : MyChatRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        userId = intent.getStringExtra("myUserId")
        chattingList = intent.getSerializableExtra("chattingList") as ArrayList<Chatting>

        //item add
        chatAdapter = MyChatRecyclerViewAdapter(chattingList!!)
        chatAdapter.chatListClickListener = object : MyChatRecyclerViewAdapter.ChatListClickListener{
            override fun onChatListClick(position: Int, item: Chatting) {
                var msgIntent = Intent(this@ChatSearchActivity, MessageActivity::class.java)
                msgIntent.putExtra("roomIndex", item.chat_index)
                msgIntent.putExtra("friendUid", item.chat_other_id)
                msgIntent.putExtra("friendName", item.chat_title)
                msgIntent.putExtra("myUid", userId)
                startActivity(msgIntent)
            }

            override fun onChatListLongClick(position: Int, item: Chatting) {

            }

        }

        binding.chatsearchRecyclerview.adapter = chatAdapter
        val linearLayoutManager = LinearLayoutManager(this@ChatSearchActivity)
        binding.chatsearchRecyclerview.layoutManager = linearLayoutManager
        binding.chatsearchRecyclerview.setHasFixedSize(true)


        //검색기능 init
        binding.chatsearchEtSearchName.addTextChangedListener {
            var name :String = binding.chatsearchEtSearchName.text.toString()
            chatAdapter.filter(name)
        }

        binding.chatsearchIvBack.setOnClickListener {
            finish()
        }

    }
}
