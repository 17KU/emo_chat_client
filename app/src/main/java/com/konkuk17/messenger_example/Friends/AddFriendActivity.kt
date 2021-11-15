package com.konkuk17.messenger_example.Friends

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.Main.MainActivity
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityAddFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddFriendActivity : AppCompatActivity() {

    val myIdViewModel: IdViewModel by viewModels<IdViewModel>()
    var user_id : String = ""



    lateinit var binding : ActivityAddFriendBinding
    var friendList : ArrayList<FriendRecycleViewData>? = null
    //val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){

        //friendList= intent.getSerializableExtra("friendList") as ArrayList<FriendRecycleViewData>
        user_id = intent.getStringExtra("myUid").toString()

        //retrofit 연결
        var retrofit = Retrofit.Builder()
            .baseUrl("http://203.252.166.201:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var friendService = retrofit.create(FriendService::class.java)


        binding.apply{


            addFriendBack.setOnClickListener{
                finish()
            }

            addBtn.setOnClickListener {
                var add_friend_id = addFriendEtxt.text.toString()

                //API 호출
                friendService.AddFriend(user_id,add_friend_id).enqueue(object :
                    Callback<AddFriendOutput> {
                    override fun onResponse(call: Call<AddFriendOutput>, response: Response<AddFriendOutput>) {

                        var add_friend = response.body()

                        //Toast.makeText(this@FriendFragment.requireContext(),user_id + " " + response.body()?.user_id + " " +response.body()?.add_friend_id,Toast.LENGTH_LONG).show()

                        var dialog = AlertDialog.Builder(this@AddFriendActivity)

                        //친구추가 성공 시
                        if(add_friend?.code.equals("0000")){
                            dialog.setTitle("친구추가")
                            dialog.setMessage("id = "+add_friend?.add_friend_id)
                            dialog.setPositiveButton("OK"){_,_->}
                            dialog.show()

                            //FriendListUpdate(friendService,friendAdapter)

                        }
                        //친구추가 실패 시
                        else{
                            dialog.setTitle("실패")
                            dialog.setMessage(user_id + " "+ add_friend?.code + "   " + add_friend?.msg)
                            dialog.setPositiveButton("OK"){_,_->}
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<AddFriendOutput>, t: Throwable) {

                    }
                })
            }
        }


    }

}