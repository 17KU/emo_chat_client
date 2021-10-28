package com.konkuk17.messenger_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.konkuk17.messenger_example.Login.LoginActivity
import com.konkuk17.messenger_example.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    lateinit var binding :ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startLoading()
    }

    fun startLoading(){
        var handler : Handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                var myintent = Intent(this@LoadingActivity, LoginActivity::class.java)
                startActivity(myintent)
                finish()
            }
        }, 2000)
    }

}