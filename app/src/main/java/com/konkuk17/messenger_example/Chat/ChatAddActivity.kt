package com.konkuk17.messenger_example.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatAddBinding

class ChatAddActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatAddBinding
    var selectedFriend : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){

    }
}