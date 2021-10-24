package com.konkuk17.messenger_example.ChatRoom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.konkuk17.messenger_example.Chat.MyChatRecyclerViewAdapter
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityMessageBinding
import java.util.ArrayList
import kotlin.math.log

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

        //입력창 bind
        var msgEditTextbind = binding.msgactiEtMsg
        var msgRecyclerViewBind = binding.messageRecyclerview


        checkChatRoom()

        //이거 checkChatRoom 안으로 옮기기
        var messageAdapter = MessageRecyclerViewAdapter(this@MessageActivity,chatRoomUid)
        msgRecyclerViewBind.adapter = messageAdapter

        var linearLayoutManager = LinearLayoutManager(this@MessageActivity)
        msgRecyclerViewBind.layoutManager = linearLayoutManager
        msgRecyclerViewBind.setHasFixedSize(true)

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
                            //Toast.makeText(this@MessageActivity,"success",Toast.LENGTH_LONG).show()
                            Log.d("fire","성공")
                        }
                        .addOnFailureListener{
                            //Toast.makeText(this@MessageActivity,"fail",Toast.LENGTH_LONG).show()
                            Log.d("fire","실패")
                        }
                }else{
                    Toast.makeText(this@MessageActivity,chatRoomUid,Toast.LENGTH_LONG).show()

                    if(msgEditTextbind.text.toString().equals("")){
                        //입력이 빈칸
                    }
                    else{
                        //입력 있을 때
                    var comment : ChatModel.Comment = ChatModel().Comment(myUid, msgEditTextbind.text.toString())
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment)
                    }
                }
            }


        }
    }



    fun checkChatRoom(){

        //user(보내는 쪽) id가 포함된 채팅방 목록 정렬
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("roomIndex").equalTo(roomindex).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    /*

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
                    Log.d("fire","체크함수 안")
                    chatRoomUid = item.key.toString()
                    Log.d("fire",chatRoomUid)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("fire","chech chat room error")
            }
        })
    }

    class MessageRecyclerViewAdapter(
        private var context: Context,
        //private var comments: List<ChatModel.Comment>,
        private var chatRoomUid: String
        ): RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder>(){

        //var comments :List<ChatModel.Comment> = ArrayList<ChatModel.Comment>()
        lateinit var comments: MutableList<ChatModel.Comment>

        init{
            //comments = ArrayList<ChatModel.Comment>()
            comments = ArrayList<ChatModel.Comment>()

            FirebaseDatabase.getInstance().getReference().child("chatroom").child(chatRoomUid).child("comments").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    Log.d("fire","in adapter : "+chatRoomUid)

                    for(item in snapshot.children){
                        Log.d("fire",item.getValue().toString())
                        comments.add(item.getValue() as ChatModel.Comment)
                    }
                    notifyDataSetChanged();
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val message = itemView.findViewById<TextView>(R.id.messageItem_textView)

            fun bind(data:ChatModel.Comment, context:Context){
                message.text = data.message
            }



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_message, null)
            return MyViewHolder(view)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(comments[position],context)
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }
}


