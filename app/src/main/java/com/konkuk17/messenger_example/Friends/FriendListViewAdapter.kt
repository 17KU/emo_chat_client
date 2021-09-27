package com.konkuk17.messenger_example.Friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.konkuk17.messenger_example.R

class FriendListViewAdapter(val context: Context, val friendlist:ArrayList<FriendRecycleViewData>) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.friendrecycle_item,null)

        val friend_name = view.findViewById<TextView>(R.id.friend_name)
        val friend_img = view.findViewById<ImageView>(R.id.friend_img)

        val friends = friendlist[position]
        val resourceId = context.resources.getIdentifier(friends.photo,"drawable",context.packageName)
        friend_img.setImageResource(resourceId)
        friend_name.text = friends.name

        return view
    }


    override fun getCount(): Int {
        return friendlist.size
    }

    override fun getItem(position: Int): Any {
        return friendlist[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}
