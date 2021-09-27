package com.konkuk17.messenger_example.Chat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.konkuk17.messenger_example.databinding.FragmentChatBinding
import org.w3c.dom.Text

class MyChatRecyclerViewAdapter(
    private var values: List<Chatting>
) : RecyclerView.Adapter<MyChatRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = values[position]
        holder.chat_title.text = item.chat_title
    }

    override fun getItemCount(): Int = values.size

    inner class MyViewHolder(binding: FragmentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var chat_title : TextView
        init{
            chat_title = binding.frChatTvChatTitle
        }
    }

}