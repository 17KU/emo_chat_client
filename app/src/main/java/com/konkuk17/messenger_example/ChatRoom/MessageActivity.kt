package com.konkuk17.messenger_example.ChatRoom

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
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
        //recyclerview bind
        var msgRecyclerViewBind = binding.messageRecyclerview




        checkChatRoom(msgRecyclerViewBind)

        binding.msgactiBtnSubmit.setOnClickListener {


            if(!chatRoomUid.equals("")){
                if(msgEditTextbind.text.toString().equals("")){
                    //입력이 빈칸
                }
                else{
                    //입력 있을 때
                    var comment : ChatModel.Comment = ChatModel().Comment(myUid, msgEditTextbind.text.toString(),"1")
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener {task->
                        if(task.isSuccessful){
                            msgEditTextbind.setText("")

                        }
                    }

                }
            }

        }


        binding.emoImg1.setOnClickListener {
            
        }




    }



    fun checkChatRoom(msgRecyclerViewBind: RecyclerView){


        //user(보내는 쪽) id가 포함된 채팅방 목록 정렬
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("roomIndex").equalTo(roomindex).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(!snapshot.exists()){
                    //채팅방 없으면 파이어베이스에 만들어주기
                    if(myUid != null && friendUid != null) {
                        var chatModel: ChatModel = ChatModel()
                        chatModel.roomIndex = roomindex
                        chatModel.users[myUid!!] = true
                        chatModel.users[friendUid!!] = true


                        if(chatRoomUid.equals("")) {
                            Log.d("fire","룸아이디 빈칸")

                            var pushKey = FirebaseDatabase.getInstance().getReference().child("chatrooms").push()
                            chatRoomUid = pushKey.key.toString()
                            pushKey.setValue(chatModel)

                            //실시간으로 메시지 갱신하기 위한 recycler view 어뎁터 연결
                            messageAdapter = MessageRecyclerViewAdapter(this@MessageActivity,chatRoomUid,myUid,friendUid,friendName, msgRecyclerViewBind,binding)
                            msgRecyclerViewBind.adapter = messageAdapter

                            linearLayoutManager = LinearLayoutManager(this@MessageActivity)
                            msgRecyclerViewBind.layoutManager = linearLayoutManager
                            msgRecyclerViewBind.setHasFixedSize(true)

                            messageAdapter.notifyDataSetChanged()
                        }

                    }

                }
                else {
                    for (item in snapshot.children) {

                        Log.d("fire", "체크함수 안")
                        chatRoomUid = item.key.toString()
                        Log.d("fire", chatRoomUid)

                        //실시간으로 메시지 갱신하기 위한 recycler view 어뎁터 연결
                        messageAdapter = MessageRecyclerViewAdapter(
                            this@MessageActivity,
                            chatRoomUid,
                            myUid,
                            friendUid,
                            friendName,
                            msgRecyclerViewBind,
                            binding
                        )
                        msgRecyclerViewBind.adapter = messageAdapter

                        linearLayoutManager = LinearLayoutManager(this@MessageActivity)
                        msgRecyclerViewBind.layoutManager = linearLayoutManager
                        msgRecyclerViewBind.setHasFixedSize(true)

                        messageAdapter.notifyDataSetChanged()

                    }
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

        private var msgRecyclerViewBind: RecyclerView,
        private var msgViewBinding: ActivityMessageBinding

        ): RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder>(){

        //var comments :List<ChatModel.Comment> = ArrayList<ChatModel.Comment>()
        var comments: MutableList<ChatModel.Comment>

        var emoService: EmoService
        var retrofit: Retrofit.Builder

        init{
            //comments = ArrayList<ChatModel.Comment>()
            comments = ArrayList<ChatModel.Comment>()


            getMessageList()

            retrofit = Retrofit.Builder()

            emoService = retrofit.baseUrl("http://203.252.166.72:80")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmoService::class.java)

        }

        //메시지 가져오기
        fun getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    Log.d("fire","in adapter : "+chatRoomUid)


                    for(item in snapshot.children){
                        Log.d("fire",item.getValue().toString())

                        var tmp : ChatModel.Comment
                        tmp = ChatModel().Comment("","","")

                        tmp.uid = item.child("uid").value.toString()
                        tmp.message = item.child("message").value.toString()
                        tmp.m_type = item.child("m_type").value.toString()

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

            private val emptyView = itemView.findViewById<View>(R.id.empty_view)

            private val emoImage = itemView.findViewById<ImageView>(R.id.messageItem_emo_img)

            fun bind(data:ChatModel.Comment, context:Context){


                //내 아이디이면
                if(data.uid.equals(myUid)){
                    //메세지일때
                    if(data.m_type.equals("1")){
                        emoImage.visibility = View.GONE
                        itemLayout.gravity = Gravity.RIGHT

                        message.text = data.message
                        message.setBackgroundResource(R.drawable.left_bubble)


                        emoBtn.visibility = View.VISIBLE
                        emoBtn.gravity = Gravity.RIGHT

                        emptyView.visibility = View.VISIBLE

                        msgViewBinding.messageViewFramelayout.visibility = View.GONE
                        msgViewBinding.msgactiBtnSubmit.visibility = View.VISIBLE
                        msgViewBinding.msgactiEtMsg.visibility = View.VISIBLE

                        emoBtn.setOnClickListener {

                            emoService.GetEmotion(data.message.toString()).enqueue(object:
                                Callback<EmoOutput> {
                                override fun onResponse(
                                    call: Call<EmoOutput>,
                                    response: Response<EmoOutput>
                                ) {
                                    var emotion = response.body()

                                    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(msgViewBinding.msgactiEtMsg.windowToken,0)



                                    msgViewBinding.messageViewFramelayout.visibility = View.VISIBLE
                                    msgViewBinding.msgactiBtnSubmit.visibility = View.GONE
                                    msgViewBinding.msgactiEtMsg.visibility = View.GONE

                                    var emo_txt = msgViewBinding.messageViewFramelayout.findViewById<TextView>(R.id.messageView_emotion)
                                    var emo_img1 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img1)
                                    var emo_img2 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img2)
                                    var emo_img3 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img3)
                                    var emo_img4 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img4)
                                    var emo_img5 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img5)
                                    var emo_img6 = msgViewBinding.messageViewFramelayout.findViewById<ImageView>(R.id.emo_img6)

                                    if(emotion?.emotion.toString().equals("0")){
                                        Log.d("fire","놀람")
                                        emo_txt.text = "놀라움"

                                        emo_img1.setImageResource(R.drawable.money)
                                        emo_img2.setImageResource(R.drawable.money)
                                        emo_img3.setImageResource(R.drawable.money)
                                        emo_img4.setImageResource(R.drawable.money)
                                        emo_img5.setImageResource(R.drawable.money)
                                        emo_img6.setImageResource(R.drawable.money)
                                    }
                                    else if(emotion?.emotion.toString().equals("1")){
                                        Log.d("fire","분노")
                                        emo_txt.text = "화남"

                                        emo_img1.setImageResource(R.drawable.book)
                                        emo_img2.setImageResource(R.drawable.book)
                                        emo_img3.setImageResource(R.drawable.book)
                                        emo_img4.setImageResource(R.drawable.book)
                                        emo_img5.setImageResource(R.drawable.book)
                                        emo_img6.setImageResource(R.drawable.book)
                                    }
                                    else if(emotion?.emotion.toString().equals("2")){
                                        Log.d("fire","불안")
                                        emo_txt.text = "불안함"

                                        emo_img1.setImageResource(R.drawable.money)
                                        emo_img2.setImageResource(R.drawable.money)
                                        emo_img3.setImageResource(R.drawable.money)
                                        emo_img4.setImageResource(R.drawable.money)
                                        emo_img5.setImageResource(R.drawable.money)
                                        emo_img6.setImageResource(R.drawable.money)
                                    }
                                    else if(emotion?.emotion.toString().equals("3")){
                                        Log.d("fire","슬픔")
                                        emo_txt.text = "슬픔"

                                        emo_img1.setImageResource(R.drawable.money)
                                        emo_img2.setImageResource(R.drawable.money)
                                        emo_img3.setImageResource(R.drawable.money)
                                        emo_img4.setImageResource(R.drawable.money)
                                        emo_img5.setImageResource(R.drawable.money)
                                        emo_img6.setImageResource(R.drawable.money)
                                    }
                                    else if(emotion?.emotion.toString().equals("4")){
                                        Log.d("fire","중립")
                                        emo_txt.text = "중립"

                                        emo_img1.setImageResource(R.drawable.money)
                                        emo_img2.setImageResource(R.drawable.money)
                                        emo_img3.setImageResource(R.drawable.money)
                                        emo_img4.setImageResource(R.drawable.money)
                                        emo_img5.setImageResource(R.drawable.money)
                                        emo_img6.setImageResource(R.drawable.money)

                                    }else if(emotion?.emotion.toString().equals("5")){
                                        Log.d("fire","행복")
                                        emo_txt.text = "행복함"

                                        emo_img1.setImageResource(R.drawable.money)
                                        emo_img2.setImageResource(R.drawable.money)
                                        emo_img3.setImageResource(R.drawable.money)
                                        emo_img4.setImageResource(R.drawable.money)
                                        emo_img5.setImageResource(R.drawable.money)
                                        emo_img6.setImageResource(R.drawable.money)
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
                    else if(data.m_type.equals("2")){
                        emoImage.visibility = View.VISIBLE
                        msgViewBinding.messageViewFramelayout.visibility = View.INVISIBLE
                    }
                }
                //친구 아이디이면
                else if(data.uid.equals(friendUid)){
                    emoImage.visibility = View.GONE
                    pImage.setImageResource(R.drawable.ic_baseline_person_24)
                    name.text = friendName
                    message.text = data.message
                    message.setBackgroundResource(R.drawable.right_bubble)
                    linearLayout.visibility = View.VISIBLE

                    itemLayout.gravity = Gravity.LEFT

                    emoBtn.visibility = View.INVISIBLE

                    msgViewBinding.messageViewFramelayout.visibility = View.GONE
                    msgViewBinding.msgactiBtnSubmit.visibility = View.VISIBLE
                    msgViewBinding.msgactiEtMsg.visibility = View.VISIBLE

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


