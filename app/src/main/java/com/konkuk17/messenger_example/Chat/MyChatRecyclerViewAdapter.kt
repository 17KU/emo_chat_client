package com.konkuk17.messenger_example.Chat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.konkuk17.messenger_example.databinding.FragmentChatBinding
import org.w3c.dom.Text

class MyChatRecyclerViewAdapter(
    private var values: ArrayList<Chatting>
) : RecyclerView.Adapter<MyChatRecyclerViewAdapter.MyViewHolder>() {

    interface ChatListClickListener{
        fun onChatListClick(position : Int, item : Chatting)
    }

    var chatListClickListener : ChatListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = values[position]
        holder.chat_title.text = item.chat_title

        holder.binding.frChatLayout.setOnClickListener {
            chatListClickListener?.onChatListClick(position, values[position])
        }
    }

    override fun getItemCount(): Int = values.size

    inner class MyViewHolder(val binding: FragmentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var chat_title : TextView
        init{
            chat_title = binding.frChatTvChatTitle
        }
    }

}