package com.konkuk17.messenger_example.ChatRoom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.konkuk17.messenger_example.R
import com.konkuk17.messenger_example.databinding.ActivityMessageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class MessageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMessageBinding

    var friendUid : String? = ""
    var myUid : String? = ""
    var chatRoomUid : String =""
    var roomindex : String=""
    var friendName: String=""
    lateinit var messageAdapter : MessageRecyclerViewAdapter
    lateinit var linearLayoutManager : LinearLayoutManager

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
        friendName = intent.getStringExtra("friendName").toString()

        //입력창 bind
        var msgEditTextbind = binding.msgactiEtMsg
        var msgRecyclerViewBind = binding.messageRecyclerview


        checkChatRoom(msgRecyclerViewBind)

        //이거 checkChatRoom 안으로 옮기기


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
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener {task->
                            if(task.isSuccessful){
                                msgEditTextbind.setText("")


                            }

                        }


                    }
                }

                /*
                messageAdapter = MessageRecyclerViewAdapter(this@MessageActivity,chatRoomUid)
                msgRecyclerViewBind.adapter = messageAdapter

                linearLayoutManager = LinearLayoutManager(this@MessageActivity)
                msgRecyclerViewBind.layoutManager = linearLayoutManager
                msgRecyclerViewBind.setHasFixedSize(true)

                messageAdapter.notifyDataSetChanged()

                 */
            }


        }
    }



    fun checkChatRoom(msgRecyclerViewBind: RecyclerView ){

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

                    messageAdapter = MessageRecyclerViewAdapter(this@MessageActivity,chatRoomUid,myUid,friendUid,friendName, msgRecyclerViewBind)
                    msgRecyclerViewBind.adapter = messageAdapter

                    linearLayoutManager = LinearLayoutManager(this@MessageActivity)
                    msgRecyclerViewBind.layoutManager = linearLayoutManager
                    msgRecyclerViewBind.setHasFixedSize(true)

                    messageAdapter.notifyDataSetChanged()

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
        private var chatRoomUid: String,
        private var myUid: String?,
        private var friendUid: String?,
        private var friendName: String,

        private var msgRecyclerViewBind: RecyclerView

        ): RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder>(){

        //var comments :List<ChatModel.Comment> = ArrayList<ChatModel.Comment>()
        lateinit var comments: MutableList<ChatModel.Comment>

        lateinit var emoService: EmoService
        lateinit var retrofit: Retrofit.Builder

        init{
            //comments = ArrayList<ChatModel.Comment>()
            comments = ArrayList<ChatModel.Comment>()


            getMessageList()

            retrofit = Retrofit.Builder()

            emoService = retrofit.baseUrl("http://203.252.166.72:80")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmoService::class.java)

        }

        fun getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    Log.d("fire","in adapter : "+chatRoomUid)


                    for(item in snapshot.children){
                        Log.d("fire",item.getValue().toString())

                        var tmp : ChatModel.Comment
                        tmp = ChatModel().Comment("","")

                        tmp.uid = item.child("uid").value.toString()
                        tmp.message = item.child("message").value.toString()

                        comments.add(tmp)

                    }
                    //새로고침(메시지 갱신)
                    notifyDataSetChanged();
                    Log.d("fire","in adapter : for is end")
                    msgRecyclerViewBind.scrollToPosition(comments.size-1)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("fire","faild adapter")
                }
            })
        }

        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val message = itemView.findViewById<TextView>(R.id.messageItem_textView)
            private val name = itemView.findViewById<TextView>(R.id.messageItem_name)
            private val pImage = itemView.findViewById<ImageView>(R.id.messageItem_profile_image)
            private val linearLayout = itemView.findViewById<LinearLayout>(R.id.messageItem_linear)

            private val itemLayout = itemView.findViewById<LinearLayout>(R.id.messageItem_mainlayout)

            private val emoBtn = itemView.findViewById<Button>(R.id.messageItem_emo_btn)

            fun bind(data:ChatModel.Comment, context:Context){

                //내 아이디이면
                if(data.uid.equals(myUid)){
                    //item_layout.gravity = Gravity.RIGHT
                    itemLayout.setHorizontalGravity(Gravity.RIGHT)

                    message.text = data.message
                    message.setBackgroundResource(R.drawable.left_bubble)


                    emoBtn.setOnClickListener {
                        emoService.GetEmotion(data.message.toString()).enqueue(object:
                            Callback<EmoOutput> {
                            override fun onResponse(
                                call: Call<EmoOutput>,
                                response: Response<EmoOutput>
                            ) {
                                var emotion = response.body()
                                Log.d("fire",emotion?.emotion.toString())
                                if(emotion?.emotion.toString().equals("0")){
                                    Log.d("fire","놀람")
                                }
                                else if(emotion?.emotion.toString().equals("1")){
                                    Log.d("fire","분노")
                                }
                                else if(emotion?.emotion.toString().equals("2")){
                                    Log.d("fire","불안")
                                }
                                else if(emotion?.emotion.toString().equals("3")){
                                    Log.d("fire","슬픔")
                                }
                                else if(emotion?.emotion.toString().equals("4")){
                                    Log.d("fire","중립")
                                }else if(emotion?.emotion.toString().equals("5")){
                                    Log.d("fire","행복")
                                }



                            }

                            override fun onFailure(call: Call<EmoOutput>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                            }
                        )
                    }
                    linearLayout.visibility = View.INVISIBLE
                }
                //친구 아이디이면
                else if(data.uid.equals(friendUid)){
                    pImage.setImageResource(R.drawable.ic_baseline_person_24)
                    name.text = friendName
                    message.text = data.message
                    message.setBackgroundResource(R.drawable.right_bubble)
                    linearLayout.visibility = View.VISIBLE

                    //item_layout.gravity = Gravity.LEFT
                    itemLayout.setHorizontalGravity(Gravity.LEFT)

                    emoBtn.visibility = View.INVISIBLE
                }
            }



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_message, null)
            return MyViewHolder(view)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(comments[position],context,)
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }
}


