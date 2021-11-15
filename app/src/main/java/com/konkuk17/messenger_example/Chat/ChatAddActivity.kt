package com.konkuk17.messenger_example.Chat

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
import com.konkuk17.messenger_example.Friends.FriendRecycleViewAdapter
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.Main.MainActivity
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatAddBinding
import com.konkuk17.messenger_example.databinding.FriendrecycleItemInChattingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class ChatAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatAddBinding
    var selectedFriend: String? = null
    var friendList: ArrayList<FriendRecycleViewData>? = null
    var userId: String? = null
    var friendName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        userId = intent.getStringExtra("myUserId")
        var temp = intent.getSerializableExtra("friendList")

        if(temp != null){
            friendList = temp as ArrayList<FriendRecycleViewData>
        }
        else{
            friendList = arrayListOf<FriendRecycleViewData>()
        }

        //리사이클러뷰 init
        var myAdapter = FriendRVAdapter(friendList!!)
        myAdapter.listener = object : FriendRVAdapter.onFriendSelectedListener {
            override fun onFriendSelected(myfriend: FriendRecycleViewData) {
                var alertDialog = AlertDialog.Builder(this@ChatAddActivity)
                    .setTitle("채팅방 생성")
                    .setMessage(myfriend.name + "님과 채팅방을 생성하시겠습니까?")
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                        Log.d("채팅방 생성", "채팅방 생성을 취소했습니다.")
                    })
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        Log.d("채팅방 생성", "채팅방을 생성했습니다.")
                        selectedFriend = myfriend.id
                        friendName = myfriend.name
                        insertChatting(userId!!, selectedFriend!!)
                    })
                alertDialog.show()
            }
        }

        binding.chataddRecyclerview.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(this@ChatAddActivity, LinearLayoutManager.VERTICAL, false)
        }

        //검색기능 init
        binding.chataddEtSearchName.addTextChangedListener {
            var name :String = binding.chataddEtSearchName.text.toString()
            myAdapter.filter(name)
        }

        binding.chataddIvBack.setOnClickListener {
            finish()
        }

    }

    fun insertChatting(myUserId: String, myFriendId: String) {
        //retrofit 객체 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.17.107:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var chatService = retrofit.create(ChatService::class.java)


        Log.d("userId", myUserId)
        Log.d("friendId", myFriendId)

        chatService.insertChatList(myUserId, myFriendId).enqueue(object : Callback<Chatting> {

            override fun onResponse(call: Call<Chatting>, response: Response<Chatting>) {
                val chatting: Chatting? = response.body()
                if (chatting != null) {
                    if (chatting?.code != "0001") {

                        var newChatting = Chatting(
                            chatting.chat_index,
                            chatting.chat_title,
                            chatting.chat_other_id,
                            chatting.code,
                            chatting.msg
                        )

                        Log.d(
                            "retrofit",
                            "chat_index : " + chatting.chat_index + ", chat_tilte : " + chatting.chat_title +
                                    ", other : " + chatting.chat_other_id + ", code : " + chatting.code + ", msg : " + chatting.msg
                        )

                        var msgIntent = Intent(this@ChatAddActivity, MessageActivity::class.java)
                        msgIntent.putExtra("roomIndex", newChatting.chat_index)
                        msgIntent.putExtra("friendUid", newChatting.chat_other_id)
                        msgIntent.putExtra("friendName", newChatting.chat_title)
                        msgIntent.putExtra("myUid", userId)
                        startActivity(msgIntent)
                        finish()
                        Log.d("retrofit", "code : "+ chatting?.code)
                        Log.d("retrofit", "msg : "+ chatting?.msg)
                    }
                    else if (chatting?.code == "0001") {
                        Toast.makeText(this@ChatAddActivity, "친구 관계가 아닙니다.", Toast.LENGTH_SHORT)
                        Log.d("retrofit", "친구 관계가 아닙니다.")
                    }
                } else {
                    Log.d("retrofit", "아무것도 없음")
                }

            }

            override fun onFailure(call: Call<Chatting>, t: Throwable) {
                Log.d("retrofit", "통신 실패")
                Log.d("retrofit", t.message.toString())
            }


        })


    }

}


class FriendRVAdapter(var friendList: ArrayList<FriendRecycleViewData>) :
    RecyclerView.Adapter<FriendRVAdapter.MyViewHolder>() {
    var displayFriendList = ArrayList<FriendRecycleViewData>()

    init {
        displayFriendList.addAll(friendList)
    }

    var listener: onFriendSelectedListener? = null

    interface onFriendSelectedListener {
        fun onFriendSelected(myfriend: FriendRecycleViewData)
    }

    inner class MyViewHolder(var itemBinding: FriendrecycleItemInChattingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemBinding =
            FriendrecycleItemInChattingBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemBinding.friendNameInChatting.text = displayFriendList[position].name
        holder.itemBinding.friendInChatLayout.setOnClickListener {
            listener?.onFriendSelected(displayFriendList[position])
        }
    }

    override fun getItemCount(): Int {
        return displayFriendList.size
    }

    fun filter(name : String){
        displayFriendList.clear()
        if(name.length == 0){
            displayFriendList.addAll(friendList)
        }
        else{
            for(friend in friendList){
                if(friend.name.contains(name)){
                    displayFriendList.add(friend)
                }
            }
        }

        notifyDataSetChanged()
    }

}

