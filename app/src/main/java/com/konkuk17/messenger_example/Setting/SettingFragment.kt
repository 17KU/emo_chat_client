package com.konkuk17.messenger_example.Setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.konkuk17.messenger_example.ChatRoom.MessageActivity
import com.konkuk17.messenger_example.Main.IdViewModel
import com.konkuk17.messenger_example.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    val myIdViewModel: IdViewModel by activityViewModels<IdViewModel>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(IdViewModel::class.java)
        binding = FragmentSettingBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    fun init(){
        binding.apply{
            chatlistIvAddChat.setOnClickListener{
                val intent = Intent(this@SettingFragment.requireContext(),MessageActivity::class.java)
                intent.putExtra("myUid","3")
                intent.putExtra("friendUid","1")
                intent.putExtra("roomIndex","13")
                intent.putExtra("friendName","333")

                startActivity(intent)
            }
        }
    }
}