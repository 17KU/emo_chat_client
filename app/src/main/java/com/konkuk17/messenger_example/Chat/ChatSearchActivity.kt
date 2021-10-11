package com.konkuk17.messenger_example.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityChatSearchBinding

class ChatSearchActivity : AppCompatActivity() {
    lateinit var binding : ActivityChatSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){

    }
}