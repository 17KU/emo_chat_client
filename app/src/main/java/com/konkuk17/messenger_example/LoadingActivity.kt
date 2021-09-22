package com.konkuk17.messenger_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.konkuk17.messenger_example.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    lateinit var binding :ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}