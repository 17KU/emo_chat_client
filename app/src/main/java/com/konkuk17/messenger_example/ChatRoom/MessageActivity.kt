package com.konkuk17.messenger_example.ChatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.konkuk17.messenger_example.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMessageBinding

    var friendUid : String? = ""
    var myUid : String? = ""
    var chatRoomUid : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        myUid = intent.getStringExtra("myUid")
        friendUid = intent.getStringExtra("friendUid")

        binding.msgactiEtMsg

        binding.msgactiBtnSubmit.setOnClickListener {
            if(myUid != null && friendUid != null) {
                var chatModel: ChatModel = ChatModel()
                chatModel.users[myUid!!] = true
                chatModel.users[friendUid!!] = true

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel)

                val database = Firebase.database
                //val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference()
                myRef.setValue("test message")


                Toast.makeText(this@MessageActivity,myRef.toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(this@MessageActivity,myUid + " " + friendUid, Toast.LENGTH_LONG).show()


            }

        }
    }


    fun checkChatRoom(){
        //FirebaseDatabase.getInstance().getReference().child("")
    }
}