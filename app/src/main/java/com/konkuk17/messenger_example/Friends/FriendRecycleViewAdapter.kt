package com.konkuk17.messenger_example.Friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk17.messenger_example.Chat.MyChatRecyclerViewAdapter
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.FragmentFriendBinding

class FriendRecycleViewAdapter(
    private var context: Context,
    private var dataList: ArrayList<FriendRecycleViewData>
) : RecyclerView.Adapter<FriendRecycleViewAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val friendName = itemView.findViewById<TextView>(R.id.friend_name)
        private val friendImg = itemView.findViewById<ImageView>(R.id.friend_img)
        private val friendFavorite = itemView.findViewById<TextView>(R.id.favorite_state)

        fun bind(friendRecycleViewData: FriendRecycleViewData, context:Context){
            friendName.text = friendRecycleViewData.name
            friendFavorite.text = friendRecycleViewData.favorite
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friendrecycle_item,null)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}