package com.konkuk17.messenger_example.Main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.konkuk17.messenger_example.Chat.ChatFragment
import com.konkuk17.messenger_example.Friends.FriendFragment
import com.konkuk17.messenger_example.Setting.SettingFragment

class MainFragStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->FriendFragment()
            1->ChatFragment()
            2->SettingFragment()
            else->ChatFragment()
        }
    }
}