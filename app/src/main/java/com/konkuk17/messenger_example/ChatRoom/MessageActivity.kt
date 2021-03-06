package com.konkuk17.messenger_example.ChatRoom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.collection.LLRBNode
import com.konkuk17.messenger_example.Main.MainActivity
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
    var myName : String? = ""
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

    @SuppressLint("ClickableViewAccessibility")
    fun init(){
        myUid = intent.getStringExtra("myUid")
        friendUid = intent.getStringExtra("friendUid")
        roomindex = intent.getStringExtra("roomIndex").toString()
        friendName = intent.getStringExtra("friendName").toString()
        myName = intent.getStringExtra("myName")


        //????????? bind
        var msgEditTextbind = binding.msgactiEtMsg
        //recyclerview bind
        var msgRecyclerViewBind = binding.messageRecyclerview


        var emo_message : String = ""


        checkChatRoom(msgRecyclerViewBind)


       // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.msgactiBtnSubmit.setOnClickListener {


            if(!chatRoomUid.equals("")){
                if(msgEditTextbind.text.toString().equals("")){
                    //????????? ??????
                }
                else{
                    //?????? ?????? ???
                    var comment : ChatModel.Comment = ChatModel().Comment(myUid, msgEditTextbind.text.toString(),"1")
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener {task->
                        if(task.isSuccessful){
                            msgEditTextbind.setText("")
                            binding.messageScrollview.post{
                                binding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
                            }
                            binding.msgactiEtMsg.isFocusable = true

                        }
                    }

                }
            }

        }

        binding.messageTitleTxt.text = friendName



        var emoImgBind1 = binding.emoImg1
        var emoImgBind2 = binding.emoImg2
        var emoImgBind3 = binding.emoImg3
        var emoImgBind4 = binding.emoImg4
        var emoImgBind5 = binding.emoImg5
        var emoImgBind6 = binding.emoImg6

        var isSelected : Boolean = false


        binding.emoImg1.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "01"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "11"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "21"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "31"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "41"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "51"

            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg1.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }

        binding.emoImg2.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "02"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "12"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "22"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "32"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "42"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "52"

            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg2.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }

        binding.emoImg3.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "03"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "13"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "23"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "33"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "43"

            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "53"

            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg3.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }

        binding.emoImg4.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "04"

            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "14"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "24"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "34"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "44"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "54"
            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg4.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }
        binding.emoImg5.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "05"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "15"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "25"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "35"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "45"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "55"
            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg5.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }
        binding.emoImg6.setOnClickListener {
            if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "06"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "16"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "26"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "36"
            }
            else if(binding.messageViewEmotion.text.equals("??????")){
                emo_message = "46"
            }
            else if(binding.messageViewEmotion.text.equals("?????????")){
                emo_message = "56"
            }
            if(isSelected){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
            }
            binding.emoImg6.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            isSelected = true
        }



        binding.messageViewSendEmo.setOnClickListener {
            if(!chatRoomUid.equals("")){

                    //?????? ?????? ???
                    var comment : ChatModel.Comment = ChatModel().Comment(myUid, emo_message,"2")
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener {task->
                        if(task.isSuccessful){
                            emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                            isSelected = false
                            binding.messageViewFramelayout.visibility = View.GONE
                        }
                    }

            }
        }


        binding.messageActivityBack.setOnClickListener {
            if(binding.messageViewFramelayout.visibility==View.VISIBLE){
                emoImgBind1.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind2.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind3.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind4.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind5.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                emoImgBind6.setColorFilter(Color.parseColor("#ffffffff"), PorterDuff.Mode.MULTIPLY)
                isSelected = false
                binding.messageViewFramelayout.visibility = View.GONE
            }
            else{
                var from : String? = intent.getStringExtra("from")
                if(from != null && from == "friend") {
                    var mainIntent = Intent(this@MessageActivity, MainActivity::class.java)
                    mainIntent.putExtra("myId", myUid)
                    mainIntent.putExtra("myName", myName)
                    startActivity(mainIntent)
                }
                finish()
            }
        }

        binding.msgactiEtMsg.setOnClickListener{
            binding.messageScrollview.post{
                binding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
            }
            binding.msgactiEtMsg.requestFocus()
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        var from : String? = intent.getStringExtra("from")
        if(from != null && from == "friend") {
            var mainIntent = Intent(this@MessageActivity, MainActivity::class.java)
            mainIntent.putExtra("myId", myUid)
            mainIntent.putExtra("myName", myName)
            startActivity(mainIntent)
        }
        finish()
    }

    fun checkChatRoom(msgRecyclerViewBind: RecyclerView){


        //user(????????? ???) id??? ????????? ????????? ?????? ??????
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("roomIndex").equalTo(roomindex).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(!snapshot.exists()){
                    //????????? ????????? ????????????????????? ???????????????
                    if(myUid != null && friendUid != null) {
                        var chatModel: ChatModel = ChatModel()
                        chatModel.roomIndex = roomindex
                        chatModel.users[myUid!!] = true
                        chatModel.users[friendUid!!] = true


                        if(chatRoomUid.equals("")) {
                            Log.d("fire","???????????? ??????")

                            var pushKey = FirebaseDatabase.getInstance().getReference().child("chatrooms").push()
                            chatRoomUid = pushKey.key.toString()
                            pushKey.setValue(chatModel)

                            //??????????????? ????????? ???????????? ?????? recycler view ????????? ??????
                            messageAdapter = MessageRecyclerViewAdapter(this@MessageActivity,chatRoomUid,myUid,friendUid,friendName, msgRecyclerViewBind,binding)
                            msgRecyclerViewBind.adapter = messageAdapter

                            linearLayoutManager = LinearLayoutManager(this@MessageActivity)
                            msgRecyclerViewBind.layoutManager = linearLayoutManager
                            msgRecyclerViewBind.setHasFixedSize(true)

                            messageAdapter.notifyDataSetChanged()

                            //binding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)

                            binding.messageScrollview.post{
                                binding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
                            }
                        }

                    }

                }
                else {
                    for (item in snapshot.children) {

                        Log.d("fire", "???????????? ???")
                        chatRoomUid = item.key.toString()
                        Log.d("fire", chatRoomUid)

                        //??????????????? ????????? ???????????? ?????? recycler view ????????? ??????
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

                        binding.messageScrollview.post{
                            binding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
                        }
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

            emoService = retrofit.baseUrl("http://220.87.45.195:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmoService::class.java)

        }

        //????????? ????????????
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
                    msgRecyclerViewBind.scrollToPosition(comments.size-1)

                    msgViewBinding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)

                    //????????????(????????? ??????)
                    notifyDataSetChanged();
                    Log.d("fire","in adapter : for is end")
                    msgRecyclerViewBind.scrollToPosition(comments.size-1)

                    msgViewBinding.messageScrollview.post{
                        msgViewBinding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
                    }

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



                //??? ???????????????
                if(data.uid.equals(myUid)){
                    //???????????????
                    if(data.m_type.equals("1")){
                        linearLayout.visibility = View.GONE
                        emoImage.setImageResource(android.R.color.transparent)
                        itemLayout.gravity = Gravity.RIGHT

                        message.visibility = View.VISIBLE
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
                                        Log.d("fire","??????")
                                        emo_txt.text = "?????????"

                                        emo_img1.setImageResource(R.drawable.emo_01)
                                        emo_img2.setImageResource(R.drawable.emo_02)
                                        emo_img3.setImageResource(R.drawable.emo_03)
                                        emo_img4.setImageResource(R.drawable.emo_04)
                                        emo_img5.setImageResource(R.drawable.emo_05)
                                        emo_img6.setImageResource(R.drawable.emo_06)
                                    }
                                    else if(emotion?.emotion.toString().equals("1")){
                                        Log.d("fire","??????")
                                        emo_txt.text = "??????"

                                        emo_img1.setImageResource(R.drawable.emo_11)
                                        emo_img2.setImageResource(R.drawable.emo_12)
                                        emo_img3.setImageResource(R.drawable.emo_13)
                                        emo_img4.setImageResource(R.drawable.emo_14)
                                        emo_img5.setImageResource(R.drawable.emo_15)
                                        emo_img6.setImageResource(R.drawable.emo_16)
                                    }
                                    else if(emotion?.emotion.toString().equals("2")){
                                        Log.d("fire","??????")
                                        emo_txt.text = "?????????"

                                        emo_img1.setImageResource(R.drawable.emo_21)
                                        emo_img2.setImageResource(R.drawable.emo_22)
                                        emo_img3.setImageResource(R.drawable.emo_23)
                                        emo_img4.setImageResource(R.drawable.emo_24)
                                        emo_img5.setImageResource(R.drawable.emo_25)
                                        emo_img6.setImageResource(R.drawable.emo_26)
                                    }
                                    else if(emotion?.emotion.toString().equals("3")){
                                        Log.d("fire","??????")
                                        emo_txt.text = "??????"

                                        emo_img1.setImageResource(R.drawable.emo_31)
                                        emo_img2.setImageResource(R.drawable.emo_32)
                                        emo_img3.setImageResource(R.drawable.emo_33)
                                        emo_img4.setImageResource(R.drawable.emo_34)
                                        emo_img5.setImageResource(R.drawable.emo_35)
                                        emo_img6.setImageResource(R.drawable.emo_36)
                                    }
                                    else if(emotion?.emotion.toString().equals("4")){
                                        Log.d("fire","??????")
                                        emo_txt.text = "??????"

                                        emo_img1.setImageResource(R.drawable.emo_41)
                                        emo_img2.setImageResource(R.drawable.emo_42)
                                        emo_img3.setImageResource(R.drawable.emo_43)
                                        emo_img4.setImageResource(R.drawable.emo_44)
                                        emo_img5.setImageResource(R.drawable.emo_45)
                                        emo_img6.setImageResource(R.drawable.emo_46)

                                    }else if(emotion?.emotion.toString().equals("5")){
                                        Log.d("fire","??????")
                                        emo_txt.text = "?????????"

                                        emo_img1.setImageResource(R.drawable.emo_51)
                                        emo_img2.setImageResource(R.drawable.emo_52)
                                        emo_img3.setImageResource(R.drawable.emo_53)
                                        emo_img4.setImageResource(R.drawable.emo_54)
                                        emo_img5.setImageResource(R.drawable.emo_55)
                                        emo_img6.setImageResource(R.drawable.emo_56)
                                    }

                                    msgViewBinding.messageScrollview.post{
                                        msgViewBinding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
                                    }


                                }

                                override fun onFailure(call: Call<EmoOutput>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }
                            }
                            )
                        }

                    }
                    //??????????????????
                    else if(data.m_type.equals("2")){
                        itemLayout.gravity = Gravity.RIGHT
                        linearLayout.visibility = View.GONE

                        msgViewBinding.messageViewFramelayout.visibility = View.GONE
                        msgViewBinding.msgactiBtnSubmit.visibility = View.VISIBLE
                        msgViewBinding.msgactiEtMsg.visibility = View.VISIBLE
                        message.visibility = View.INVISIBLE
                        emoBtn.visibility = View.INVISIBLE
                        emoBtn.gravity = Gravity.RIGHT


                        emptyView.visibility = View.VISIBLE



                        var num : Int = data.message.toString().toInt()

                        when(num/10){
                            0-> when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_01)
                                2 -> emoImage.setImageResource(R.drawable.emo_02)
                                3 -> emoImage.setImageResource(R.drawable.emo_03)
                                4->emoImage.setImageResource(R.drawable.emo_04)
                                5->emoImage.setImageResource(R.drawable.emo_05)
                                6->emoImage.setImageResource(R.drawable.emo_06)
                            }
                            1->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_11)
                                2 -> emoImage.setImageResource(R.drawable.emo_12)
                                3 -> emoImage.setImageResource(R.drawable.emo_13)
                                4->emoImage.setImageResource(R.drawable.emo_14)
                                5->emoImage.setImageResource(R.drawable.emo_15)
                                6->emoImage.setImageResource(R.drawable.emo_16)
                            }
                            2->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_21)
                                2 -> emoImage.setImageResource(R.drawable.emo_22)
                                3 -> emoImage.setImageResource(R.drawable.emo_23)
                                4->emoImage.setImageResource(R.drawable.emo_24)
                                5->emoImage.setImageResource(R.drawable.emo_25)
                                6->emoImage.setImageResource(R.drawable.emo_26)
                            }
                            3->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_31)
                                2 -> emoImage.setImageResource(R.drawable.emo_32)
                                3 -> emoImage.setImageResource(R.drawable.emo_33)
                                4->emoImage.setImageResource(R.drawable.emo_34)
                                5->emoImage.setImageResource(R.drawable.emo_35)
                                6->emoImage.setImageResource(R.drawable.emo_36)
                            }
                            4->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_41)
                                2 -> emoImage.setImageResource(R.drawable.emo_42)
                                3 -> emoImage.setImageResource(R.drawable.emo_43)
                                4->emoImage.setImageResource(R.drawable.emo_44)
                                5->emoImage.setImageResource(R.drawable.emo_45)
                                6->emoImage.setImageResource(R.drawable.emo_46)
                            }
                            5->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_51)
                                2 -> emoImage.setImageResource(R.drawable.emo_52)
                                3 -> emoImage.setImageResource(R.drawable.emo_53)
                                4->emoImage.setImageResource(R.drawable.emo_54)
                                5->emoImage.setImageResource(R.drawable.emo_55)
                                6->emoImage.setImageResource(R.drawable.emo_56)
                            }

                        }


                    }
                }
                //?????? ???????????????
                else if(data.uid.equals(friendUid)){
                    //???????????????
                    if(data.m_type.equals("1")){
                        linearLayout.visibility = View.VISIBLE
                        emoImage.setImageResource(android.R.color.transparent)
                        message.visibility = View.VISIBLE

                        pImage.setImageResource(R.drawable.emo_01)
                        name.text = friendName
                        message.text = data.message
                        message.setBackgroundResource(R.drawable.right_bubble)
                        linearLayout.visibility = View.VISIBLE

                        itemLayout.gravity = Gravity.LEFT

                        emoBtn.visibility = View.GONE

                        msgViewBinding.messageViewFramelayout.visibility = View.GONE
                        msgViewBinding.msgactiBtnSubmit.visibility = View.VISIBLE
                        msgViewBinding.msgactiEtMsg.visibility = View.VISIBLE


                        emptyView.visibility = View.GONE
                    }

                    //??????????????????
                    else if(data.m_type.equals("2")){
                        linearLayout.visibility = View.VISIBLE
                        emoImage.visibility = View.VISIBLE
                        msgViewBinding.messageViewFramelayout.visibility = View.GONE
                        msgViewBinding.msgactiBtnSubmit.visibility = View.VISIBLE
                        msgViewBinding.msgactiEtMsg.visibility = View.VISIBLE
                        message.visibility = View.GONE
                        emoBtn.visibility = View.GONE

                        linearLayout.visibility = View.VISIBLE
                        pImage.setImageResource(R.drawable.emo_01)
                        name.text = friendName

                        itemLayout.gravity = Gravity.LEFT
                        emptyView.visibility = View.GONE


                        var num : Int = data.message.toString().toInt()



                        when(num/10){
                            0-> when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_01)
                                2 -> emoImage.setImageResource(R.drawable.emo_02)
                                3 -> emoImage.setImageResource(R.drawable.emo_03)
                                4->emoImage.setImageResource(R.drawable.emo_04)
                                5->emoImage.setImageResource(R.drawable.emo_05)
                                6->emoImage.setImageResource(R.drawable.emo_06)
                            }
                            1->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_11)
                                2 -> emoImage.setImageResource(R.drawable.emo_12)
                                3 -> emoImage.setImageResource(R.drawable.emo_13)
                                4->emoImage.setImageResource(R.drawable.emo_14)
                                5->emoImage.setImageResource(R.drawable.emo_15)
                                6->emoImage.setImageResource(R.drawable.emo_16)
                            }
                            2->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_21)
                                2 -> emoImage.setImageResource(R.drawable.emo_22)
                                3 -> emoImage.setImageResource(R.drawable.emo_23)
                                4->emoImage.setImageResource(R.drawable.emo_24)
                                5->emoImage.setImageResource(R.drawable.emo_25)
                                6->emoImage.setImageResource(R.drawable.emo_26)
                            }
                            3->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_31)
                                2 -> emoImage.setImageResource(R.drawable.emo_32)
                                3 -> emoImage.setImageResource(R.drawable.emo_33)
                                4->emoImage.setImageResource(R.drawable.emo_34)
                                5->emoImage.setImageResource(R.drawable.emo_35)
                                6->emoImage.setImageResource(R.drawable.emo_36)
                            }
                            4->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_41)
                                2 -> emoImage.setImageResource(R.drawable.emo_42)
                                3 -> emoImage.setImageResource(R.drawable.emo_43)
                                4->emoImage.setImageResource(R.drawable.emo_44)
                                5->emoImage.setImageResource(R.drawable.emo_45)
                                6->emoImage.setImageResource(R.drawable.emo_46)
                            }
                            5->when(num%10){
                                1 -> emoImage.setImageResource(R.drawable.emo_51)
                                2 -> emoImage.setImageResource(R.drawable.emo_52)
                                3 -> emoImage.setImageResource(R.drawable.emo_53)
                                4->emoImage.setImageResource(R.drawable.emo_54)
                                5->emoImage.setImageResource(R.drawable.emo_55)
                                6->emoImage.setImageResource(R.drawable.emo_56)
                            }

                        }
                    }
                }

                msgViewBinding.messageScrollview.post{
                    msgViewBinding.messageScrollview.fullScroll(ScrollView.FOCUS_DOWN)
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


