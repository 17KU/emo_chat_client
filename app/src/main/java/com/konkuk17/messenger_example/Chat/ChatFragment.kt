package com.konkuk17.messenger_example.Chat

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
import com.konkuk17.messenger_example.Friends.FriendRecycleViewAdapter
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.Login.Login
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.Main.MainActivity
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.Signup.MyResponse
import com.konkuk17.messenger_example.databinding.FragmentChatBinding
import com.konkuk17.messenger_example.databinding.FragmentChatListBinding
import com.konkuk17.messenger_example.databinding.FragmentFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChatFragment : Fragment() {

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()
    lateinit var retrofit : Retrofit
    lateinit var chatService : ChatService

    var items: ArrayList<Chatting> = arrayListOf<Chatting>()
    lateinit var binding : FragmentChatListBinding
    lateinit var chatAdapter : MyChatRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(IdViewModel::class.java)
        binding = FragmentChatListBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //???????????? ?????? ?????????
        retrofit = Retrofit.Builder()
            .baseUrl("http://220.87.45.195:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        chatService = retrofit.create(ChatService::class.java)

        //item add
        chatAdapter = MyChatRecyclerViewAdapter(items)
        chatAdapter.chatListClickListener = object : MyChatRecyclerViewAdapter.ChatListClickListener{
            override fun onChatListClick(position: Int, item: Chatting) {
                var msgIntent = Intent(this@ChatFragment.requireContext(), MessageActivity::class.java)
                msgIntent.putExtra("roomIndex", item.chat_index)
                msgIntent.putExtra("friendUid", item.chat_other_id)
                msgIntent.putExtra("friendName", item.chat_title)
                msgIntent.putExtra("myUid", myIdViewModel.myId.value)
                startActivity(msgIntent)
            }

            override fun onChatListLongClick(position: Int, item: Chatting) {
                var dialog = AlertDialog.Builder(this@ChatFragment.requireContext())
                    .setTitle("????????? ??????")
                    .setMessage(item.chat_title+"????????? ???????????? ?????????????????????????")
                    .setPositiveButton("??????", DialogInterface.OnClickListener { dialog, which ->
                        var myId =  myIdViewModel.myId.value.toString()
                        chatService.deleteChatList(item.chat_index!!, myId)
                            .enqueue(object : Callback<MyResponse>{
                                override fun onResponse(
                                    call: Call<MyResponse>,
                                    response: Response<MyResponse>
                                ) {
                                    var result = response.body()
                                    if (result?.code == "0000"){
                                        Toast.makeText(this@ChatFragment.requireContext(), "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                                        Log.d("?????? ????????? ??????", "?????? ??????")
                                        dataInit()

                                    }
                                    else if (result?.code == "0001"){
                                        Toast.makeText(this@ChatFragment.requireContext(), "???????????? ????????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
                                        Log.d("?????? ????????? ??????", "?????? ?????? - ?????? ??????")
                                    }
                                    else{
                                        Toast.makeText(this@ChatFragment.requireContext(), "???????????? ????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                                        Log.d("?????? ????????? ??????", "?????? ?????? - ?????? ??????")
                                    }

                                }

                                override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                    Log.d("debug", t.message.toString())
                                    Log.d("?????? ????????? ??????", "?????? ?????? - ????????? ?????? ??????")
                                    Toast.makeText(this@ChatFragment.requireContext(), "????????? ????????? ??????????????????. (?????? ?????? ??????)", Toast.LENGTH_SHORT).show()
                                }


                            })
                    })
                    .setNegativeButton("??????", DialogInterface.OnClickListener { dialog, which ->
                        Log.d("chatting", "????????????")
                    })

                dialog.show()
            }

        }
        Log.d("chatFrag", "Adpater ???????????? ??????")
        binding.fgChatRecyclerview.adapter = chatAdapter
        Log.d("chatFrag", "????????????????????? ???????????? ??????")
        val linearLayoutManager = LinearLayoutManager(this@ChatFragment.requireContext())
        binding.fgChatRecyclerview.layoutManager = linearLayoutManager
        binding.fgChatRecyclerview.setHasFixedSize(true)

        Log.d("chatFrag", "datainit ??????")
        dataInit()
        Log.d("chatFrag", "buttoninit ??????")
        buttonInit()


    }


    fun dataInit() {
        //retrofit ?????? ?????????


        var user_id : String = myIdViewModel.myId.value.toString()
        Log.d("userId", user_id)
        //var user_id : String = "youm"

        chatService.selectChatList(user_id).enqueue(object : Callback<List<Chatting>>{

            //???????????? ??????????????? ???????????? ??????. ???????????? ?????????.
            override fun onResponse(
                call: Call<List<Chatting>>,
                response: Response<List<Chatting>>
            ) {
                items.clear()
                val chattingList : List<Chatting>?  = response.body()
                if(chattingList != null) {
                    if(chattingList[0]?.code != "0001"){
                        for (item in chattingList) {
                            items.add(Chatting(item.chat_index, item.chat_title, item.chat_other_id, item.code, item.msg))
                            Log.d("Chat Item", "chat_index : " + item.chat_index + ", chat_tilte : "+item.chat_title +
                                    ", other : "+ item.chat_other_id +", code : "+ item.code + ", msg : "+item.msg)
                        }

                        chatAdapter.notifyDataSetChanged()
                    }
                }
                else{
                    Log.d("Chat Item", "???????????? ??????")
                }
            }
            //???????????? ??????????????? ???????????? ??????
            override fun onFailure(call: Call<List<Chatting>>, t: Throwable) {
                Log.d("debug", t.message.toString())
                var dialog = AlertDialog.Builder(this@ChatFragment.requireContext())
                dialog.setTitle("??????!")
                dialog.setMessage(t.message.toString())
                dialog.setPositiveButton("??????") { _, _ ->
                }
                dialog.show()
            }


        })


    }

    fun buttonInit(){

        //?????? ????????? ??????
        binding.chatlistIvAddChat.setOnClickListener {
            var friendList :ArrayList<FriendRecycleViewData>? = myIdViewModel.getFriendList()
            var user_id : String = myIdViewModel.myId.value.toString()

            var intentChatAdd = Intent(this@ChatFragment.requireContext(), ChatAddActivity::class.java)
            intentChatAdd.putExtra("friendList", friendList)
            intentChatAdd.putExtra("myUserId", user_id)
            startActivityForResult(intentChatAdd,100)
        }


        //?????? ????????? ??????
        binding.chatlistIvSearchChat.setOnClickListener {
            var user_id : String = myIdViewModel.myId.value.toString()
            var chattingList :ArrayList<Chatting> = arrayListOf()
            chattingList.addAll(items)

            var intentChatSearch = Intent(this@ChatFragment.requireContext(), ChatSearchActivity::class.java)
            intentChatSearch.putExtra("chattingList", chattingList)
            intentChatSearch.putExtra("myUserId", user_id)
            startActivity(intentChatSearch)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100->{
                dataInit()
            }
        }

    }
}