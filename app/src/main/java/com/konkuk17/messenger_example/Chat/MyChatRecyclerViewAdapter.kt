package com.konkuk17.messenger_example.Chat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.konkuk17.messenger_example.databinding.FragmentChatBinding

class MyChatRecyclerViewAdapter(
    private val values: List<Chatting>
) : RecyclerView.Adapter<MyChatRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.chat_other_id
        holder.contentView.text = item.chat_title
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}