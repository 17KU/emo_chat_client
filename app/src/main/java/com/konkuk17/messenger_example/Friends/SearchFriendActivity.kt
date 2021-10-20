package com.konkuk17.messenger_example.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivitySearchFriendBinding

class SearchFriendActivity : AppCompatActivity() {
    //val myIdViewModel: IdViewModel by viewModels<IdViewModel>()

    lateinit var binding : ActivitySearchFriendBinding
    //var friendList : ArrayList<FriendRecycleViewData>? = null

    var searchList = arrayListOf<FriendRecycleViewData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){
       //var user_id = intent.getStringExtra("myUid")

        //유저 친구 목록
        var friendList :ArrayList<FriendRecycleViewData>? = intent.getSerializableExtra("fList") as ArrayList<FriendRecycleViewData>

        //검색하고 나서 저장할 목록(출력)
        //var searchList : ArrayList<FriendRecycleViewData>

        var searchAdapter = FriendRecycleViewAdapter(this,searchList){}


        binding.apply{

            searchRecycleView.adapter = searchAdapter
            val linearLayoutManager = LinearLayoutManager(this@SearchFriendActivity)
            searchRecycleView.layoutManager = linearLayoutManager
            searchRecycleView.setHasFixedSize(true)

            searchBtn.setOnClickListener {

                searchList.clear()
                searchAdapter.notifyDataSetChanged()

                var find_friend_id = findFriendEtxt.text.toString()

                var tmplist = friendList?.filter{it.name.equals(find_friend_id)} as ArrayList<FriendRecycleViewData>

                if(tmplist!=null){
                    for(friend in tmplist){
                        searchList.apply{
                            add(FriendRecycleViewData(friend.name, friend.id, friend.favorite))
                            searchAdapter.notifyDataSetChanged()
                        }
                    }
                }



            }
        }

    }
}