package com.konkuk17.messenger_example.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivitySearchFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFriendActivity : AppCompatActivity() {
    //val myIdViewModel: IdViewModel by viewModels<IdViewModel>()
    lateinit var friendService: FriendService
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

        //retrofit 연결
        var retrofit = Retrofit.Builder()
            .baseUrl("http://203.252.166.201:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //friendService API 연결
        friendService = retrofit.create(FriendService::class.java)


        var user_id = intent.getStringExtra("myUid").toString()
        var user_name = intent.getStringExtra("myName").toString()

        //유저 친구 목록
        var friendList :ArrayList<FriendRecycleViewData> = intent.getSerializableExtra("fList") as ArrayList<FriendRecycleViewData>

        //검색하고 나서 저장할 목록(출력)
        //var searchList : ArrayList<FriendRecycleViewData>

        var searchAdapter = FriendRecycleViewAdapter(this,friendList,friendService,user_id, user_name) { friendRecycleViewData ->

            var favorite_add = friendRecycleViewData.id

            Toast.makeText(this@SearchFriendActivity, favorite_add, Toast.LENGTH_LONG).show()

            //친구 즐겨찾기 추가
            //어댑터에 익명 리스너 붙이기 위해 여기에 코드 작성
            friendService.AddFavorite(user_id, favorite_add).enqueue(object :
                Callback<AddFriendOutput> {
                override fun onResponse(
                    call: Call<AddFriendOutput>,
                    response: Response<AddFriendOutput>
                ) {
                    var favorite = response.body()

                    if (favorite?.code.equals("0002")) {

                    }

                }

                override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                }

            }
            )
        }
        binding.searchRecycleView.adapter = searchAdapter
        val linearLayoutManager = LinearLayoutManager(this@SearchFriendActivity)
        binding.searchRecycleView.layoutManager = linearLayoutManager
        binding.searchRecycleView.setHasFixedSize(true)

        binding.findFriendEtxt.addTextChangedListener{
            var name:String = binding.findFriendEtxt.text.toString()
            searchAdapter.filter(name)
        }

        /*
        binding.apply{

            searchRecycleView.adapter = searchAdapter
            val linearLayoutManager = LinearLayoutManager(this@SearchFriendActivity)
            searchRecycleView.layoutManager = linearLayoutManager
            searchRecycleView.setHasFixedSize(true)

            /*
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
            */

            findFriendEtxt.addTextChangedListener{
                var name:String = findFriendEtxt.text.toString()
                searchAdapter.filter(name)
            }
        }
        */
        binding.searchFriendBack.setOnClickListener {
            finish()
        }

    }
}