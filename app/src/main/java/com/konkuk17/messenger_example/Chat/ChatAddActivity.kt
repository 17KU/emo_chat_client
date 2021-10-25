package com.konkuk17.messenger_example.Chat

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.konkuk17.messenger_example.Friends.FriendRecycleViewAdapter
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatAddBinding
import com.konkuk17.messenger_example.databinding.FriendrecycleItemInChattingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatAddBinding
    var selectedFriend: String? = null
    var friendList: ArrayList<FriendRecycleViewData>? = null
    var userId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        userId = intent.getStringExtra("myUserId")
        friendList = intent.getSerializableExtra("friendList") as ArrayList<FriendRecycleViewData>

        if (friendList != null) {

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
                            insertChatting(userId!!, selectedFriend!!)
                        })
                    alertDialog.show()
                }
            }

            binding.chataddRecyclerview.apply {
                adapter = myAdapter
                layoutManager = LinearLayoutManager(this@ChatAddActivity, LinearLayoutManager.VERTICAL, false)
            }

        }
    }

    fun insertChatting(myUserId : String, myFriendId : String) {
        //retrofit 객체 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://203.252.166.72:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var chatService = retrofit.create(ChatService::class.java)


        Log.d("userId", myUserId)
        Log.d("friendId", myFriendId)

        chatService.insertChatList(myUserId, myFriendId).enqueue(object : Callback<List<Chatting>> {

            //웹통신에 성공했을때 실행되는 코드. 응답값을 받아옴.
            override fun onResponse(
                call: Call<List<Chatting>>,
                response: Response<List<Chatting>>
            ) {

                val chattingList : List<Chatting>?  = response.body()
                if(chattingList != null) {
                    if(chattingList[0]?.code == "0004" || chattingList[0]?.code == "0005"){
                        for (item in chattingList) {
                            var newChatting = Chatting(item.chat_index, item.chat_title, item.chat_other_id, item.code, item.msg)

                            Log.d("retrofit", "chat_index : " + item.chat_index + ", chat_tilte : "+item.chat_title +
                                    ", other : "+ item.chat_other_id +", code : "+ item.code + ", msg : "+item.msg)

                        }

                    }
                }
                else{
                    Log.d("retrofit", "아무것도 없음")
                }

            }
            //웹통신에 실패했을때 실행되는 코드
            override fun onFailure(call: Call<List<Chatting>>, t: Throwable) {
                Log.d("retrofit", "통신 실패")
                Log.d("retrofit", t.message.toString())
            }


        })


    }

}


class FriendRVAdapter(var friendList: ArrayList<FriendRecycleViewData>) :
    RecyclerView.Adapter<FriendRVAdapter.MyViewHolder>() {

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
        holder.itemBinding.friendNameInChatting.text = friendList[position].name
        holder.itemBinding.friendInChatLayout.setOnClickListener {
            listener?.onFriendSelected(friendList[position])
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

}

