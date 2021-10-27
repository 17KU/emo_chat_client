package com.konkuk17.messenger_example.Friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.konkuk17.messenger_example.Chat.MyChatRecyclerViewAdapter
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.FragmentFriendBinding

class FriendRecycleViewAdapter(
    private var context: Context,
    private var dataList: ArrayList<FriendRecycleViewData>,
    private val itemClick: (FriendRecycleViewData) -> Unit
    //private val deleteClick: (FriendRecycleViewData) -> Unit
) : RecyclerView.Adapter<FriendRecycleViewAdapter.ItemViewHolder>(){

    var allFriendList = ArrayList<FriendRecycleViewData>()
    var favoriteList = ArrayList<FriendRecycleViewData>()

    init{
        allFriendList.addAll(dataList)
        favoriteList.addAll(dataList)
    }

    interface FriendListClickListener{
        fun onFriendListLongClick(position: Int, item: FriendRecycleViewData)
    }

    var friendListClickListener : FriendListClickListener? =null

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val friendName = itemView.findViewById<TextView>(R.id.friend_name)
        private val friendImg = itemView.findViewById<ImageView>(R.id.friend_img)
        private val friendFavorite = itemView.findViewById<TextView>(R.id.favorite_state)
        private val favoriteChip = itemView.findViewById<Chip>(R.id.favorite_chip)
        private val friendItemLayout = itemView.findViewById<LinearLayout>(R.id.friend_item_layout)

        fun bind(friendRecycleViewData: FriendRecycleViewData, context:Context){
            friendName.text = friendRecycleViewData.name
            //friendFavorite.text = friendRecycleViewData.favorite


            if(friendRecycleViewData.favorite.equals("true")){
                favoriteChip.setChecked(true)
                //friendRecycleViewData.favorite = "true"
            }
            else{
                favoriteChip.setChecked(false)
                //friendRecycleViewData.favorite = "false"
            }

            favoriteChip.setOnClickListener{ itemClick(friendRecycleViewData) }

            friendItemLayout.setOnLongClickListener {
                friendListClickListener?.onFriendListLongClick(position, dataList[position])
                true
            }
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

    //SearchFriendd에서 검색을 위해 사용
    fun filter(name: String){
        dataList.clear()
        if(name.length==0){
            dataList.addAll(allFriendList)

        }
        else{
            for(friend in allFriendList){
                if(friend.name!!.contains(name)){
                    dataList.add(friend)
                }
            }
        }

        notifyDataSetChanged()
    }


    fun filter_favorite(){
        dataList.clear()

        for(friend in favoriteList){
            if(friend.favorite.equals("true")){
                dataList.add(friend)
            }
        }
        notifyDataSetChanged()
    }



}

