package com.konkuk17.messenger_example.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FriendActivity : AppCompatActivity() {
    lateinit var binding: ActivityFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){

        var retrofit = Retrofit.Builder()
            .baseUrl("http://3.36.165.136:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var friendService = retrofit.create(FriendService::class.java)

        binding.apply{
            addFriendBtn.setOnClickListener{

                var user_id = "youm"
                var add_friend_id = addFriendEtxt.editText.toString()

                friendService.AddFriend(user_id,add_friend_id).enqueue(object : Callback<AddFriend>{
                    override fun onResponse(call: Call<AddFriend>, response: Response<AddFriend>) {
                        TODO("Not yet implemented")

                        var add_friend = response.body()

                        var dialog = AlertDialog.Builder(this@FriendActivity)
                        dialog.setTitle("친구추가")
                    }

                    override fun onFailure(call: Call<AddFriend>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }

    }


}