package com.konkuk17.messenger_example.ChatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.konkuk17.messenger_example.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMessageBinding

    var friendUid : String? = ""
    var myUid : String? = ""
    var chatRoomUid : String =""
    var roomindex : String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)



        init()
    }

    fun init(){
        myUid = intent.getStringExtra("myUid")
        friendUid = intent.getStringExtra("friendUid")
        roomindex = intent.getStringExtra("roomIndex").toString()

        var bind = binding.msgactiEtMsg



        binding.msgactiBtnSubmit.setOnClickListener {

            if(myUid != null && friendUid != null) {
                var chatModel: ChatModel = ChatModel()
                chatModel.roomIndex = roomindex
                chatModel.users[myUid!!] = true
                chatModel.users[friendUid!!] = true



                if(chatRoomUid.equals("")) {
                    Log.d("fire","룸아이디 빈칸")
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push()
                        .setValue(chatModel).addOnSuccessListener {
                            Toast.makeText(this@MessageActivity,"success",Toast.LENGTH_LONG).show()
                            Log.d("fire","성공")
                        }
                        .addOnFailureListener{
                            Toast.makeText(this@MessageActivity,"fail",Toast.LENGTH_LONG).show()
                            Log.d("fire","실패")
                        }
                }else{
                    Toast.makeText(this@MessageActivity,chatRoomUid,Toast.LENGTH_LONG).show()

                    var comment : ChatModel.Comment = ChatModel().Comment(myUid, bind.text.toString())
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment)
                }
            }

            checkChatRoom()
        }
    }



    fun checkChatRoom(){


        //user(보내는 쪽) id가 포함된 채팅방 목록 정렬
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("roomIndex").equalTo(roomindex).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    /*
                    Log.d("fire","체크함수 안")
                    Log.d("fire",item.getValue().toString())
                    var chatModel : ChatModel = item.getValue() as ChatModel
                    //var chatModel : HashMap<String,Any> = item.getValue() as HashMap<String,Any>
                    Log.d("fire","체크함수 데이터체인지 안")
                    Toast.makeText(this@MessageActivity,"in checkChatRoom",Toast.LENGTH_LONG).show()
                    //상대 id가 포함된 채팅방이 있으면 chatRoomUid에 세팅
                    if(chatModel.users.containsKey(friendUid)){
                    //if(chatModel.containsKey(roomindex)){
                        Log.d("fire","이미 있는 채팅방")
                        chatRoomUid = item.key.toString()
                        Toast.makeText(this@MessageActivity,chatRoomUid,Toast.LENGTH_LONG).show()

                    }
                     */

                    chatRoomUid = item.key.toString()
                    Log.d("fire",chatRoomUid)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("fire","chech chat room error")
            }
        })
    }
}


