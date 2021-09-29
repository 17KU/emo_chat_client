package com.konkuk17.messenger_example.Chat

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.konkuk17.messenger_example.Friends.FriendRecycleViewAdapter
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData
import com.konkuk17.messenger_example.Login.Login
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.Main.MainActivity
import com.konkuk17.messenger_example.R
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
        //item add
        chatAdapter = MyChatRecyclerViewAdapter(items)

        binding.fgChatRecyclerview.adapter = chatAdapter
        val linearLayoutManager = LinearLayoutManager(this@ChatFragment.requireContext())
        binding.fgChatRecyclerview.layoutManager = linearLayoutManager
        binding.fgChatRecyclerview.setHasFixedSize(true)

        dataInit()

    }


    fun dataInit() {
        //retrofit 객체 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://3.36.165.136:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var chatService = retrofit.create(ChatService::class.java)

        var user_id : String = myIdViewModel.myId.value.toString()
        Log.d("userId", user_id)
        //var user_id : String = "youm"

        chatService.selectChatList(user_id).enqueue(object : Callback<List<Chatting>>{

            //웹통신에 성공했을때 실행되는 코드. 응답값을 받아옴.
            override fun onResponse(
                call: Call<List<Chatting>>,
                response: Response<List<Chatting>>
            ) {
                items.clear()
                val chattingList : List<Chatting>?  = response.body()
                if(chattingList != null) {
                    for (item in chattingList) {
                        items.add(Chatting(item.chat_index, item.chat_title, item.chat_other_id))
                        Log.d("Chat Item", "chat_index : " + item.chat_index + ", chat_tilte : "+item.chat_title +", other : "+ item.chat_other_id)
                    }

                    chatAdapter.notifyDataSetChanged()
                }
                else{
                    Log.d("Chat Item", "아무것도 없음")
                }
            }
            //웹통신에 실패했을때 실행되는 코드
            override fun onFailure(call: Call<List<Chatting>>, t: Throwable) {
                Log.d("debug", t.message.toString())
                var dialog = AlertDialog.Builder(this@ChatFragment.requireContext())
                dialog.setTitle("실패!")
                dialog.setMessage(t.message.toString())
                dialog.setPositiveButton("확인") { _, _ ->
                }
                dialog.show()
            }


        })


    }
}