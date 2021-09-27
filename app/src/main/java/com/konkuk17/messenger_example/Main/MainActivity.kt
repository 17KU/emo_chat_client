package com.konkuk17.messenger_example.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var textarr = arrayListOf<String>("친구", "채팅", "설정")
    var iconarr = arrayListOf<Int>(
        R.drawable.ic_baseline_people_24,
        R.drawable.ic_baseline_chat_24,
        R.drawable.ic_baseline_settings_24
    )

    val idViewModel : IdViewModel by viewModels<IdViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        binding.viewPager.adapter = MainFragStateAdapter(this)

        TabLayoutMediator(binding.tablayout, binding.viewPager){
            tab, position->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()
    }

}