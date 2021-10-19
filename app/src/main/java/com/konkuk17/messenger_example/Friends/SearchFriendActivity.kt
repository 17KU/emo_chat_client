package com.konkuk17.messenger_example.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivitySearchFriendBinding

class SearchFriendActivity : AppCompatActivity() {
    val myIdViewModel: IdViewModel by viewModels<IdViewModel>()

    lateinit var binding : ActivitySearchFriendBinding
    //var friendList : ArrayList<FriendRecycleViewData>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchFriendBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
    }

    fun init(){
        var user_id = myIdViewModel.myId.value.toString()

        //유저 친구 목록
        var friendList :ArrayList<FriendRecycleViewData>? = myIdViewModel.getFriendList()

        //검색하고 나서 저장할 목록(출력)
        var searchList : ArrayList<FriendRecycleViewData>? = null

        binding.apply{
            searchBtn.setOnClickListener {
                var find_friend_id = findFriendEtxt.text.toString()


            }
        }

    }
}