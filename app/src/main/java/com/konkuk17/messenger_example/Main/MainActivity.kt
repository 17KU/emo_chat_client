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
        R.drawable.emo_04,
        R.drawable.emo_03,
        R.drawable.emo_02
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

        //LoginActivity 에서 받아온 아이디랑 이름을 IdViewModel에 저장하기
        val myId : String = intent.getStringExtra("myId").toString()
        val myName : String = intent.getStringExtra("myName").toString()
        idViewModel.setMyId(myId)
        idViewModel.setMyNmae(myName)

        //ChatAddActivity에서 받아온 값이 있을 경우 실행
        /*
        var selectedPage = intent.getStringExtra("selectedPage")
        if (selectedPage == "chattingPage"){
            binding.tablayout.setScrollPosition(1,0f,true)
            binding.viewPager.setCurrentItem(1)
        }


         */
    }

}