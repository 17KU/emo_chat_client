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

class ChatAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatAddBinding
    var selectedFriend: String? = null
    var friendList: ArrayList<FriendRecycleViewData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
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
                            Toast.makeText(this, "채팅방 생성을 취소했습니다.", Toast.LENGTH_SHORT)
                        })
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                            Log.d("채팅방 생성", "채팅방을 생성했습니다.")
                            Toast.makeText(this, "채팅방을 생성했습니다.", Toast.LENGTH_SHORT)
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

