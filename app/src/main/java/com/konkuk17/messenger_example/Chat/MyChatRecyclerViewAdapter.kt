package com.konkuk17.messenger_example.Chat

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.konkuk17.messenger_example.Friends.FriendRecycleViewData

import com.konkuk17.messenger_example.databinding.FragmentChatBinding
import org.w3c.dom.Text

class MyChatRecyclerViewAdapter(
    private var values: ArrayList<Chatting>
) : RecyclerView.Adapter<MyChatRecyclerViewAdapter.MyViewHolder>() {

    var allChattingList = ArrayList<Chatting>()

    init {
        allChattingList.addAll(values)
        Log.d("chatFrag", "displaychatlist에 옮김")
        Log.d("chatFrag", "사이즈 : "+ allChattingList.size)
    }


    interface ChatListClickListener{
        fun onChatListClick(position : Int, item : Chatting)

        fun onChatListLongClick(position: Int, item : Chatting)
    }

    var chatListClickListener : ChatListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("chatFrag", "바인딩")
        val item = values[position]
        holder.chat_title.text = item.chat_title

        holder.binding.frChatLayout.setOnClickListener {
            chatListClickListener?.onChatListClick(position, values[position])
        }

        holder.binding.frChatLayout.setOnLongClickListener {
            chatListClickListener?.onChatListLongClick(position, values[position])
            true
        }
    }

    override fun getItemCount(): Int = values.size

    inner class MyViewHolder(val binding: FragmentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var chat_title : TextView
        init{
            chat_title = binding.frChatTvChatTitle
        }
    }

    //ChatSearchActivity에서만 사용함
    fun filter(name : String){
        values.clear()
        if(name.length == 0){
            values.addAll(allChattingList)
        }
        else{
            for(chat in allChattingList){
                if(chat.chat_title!!.contains(name)){
                    values.add(chat)
                }
            }
        }

        notifyDataSetChanged()
    }


}