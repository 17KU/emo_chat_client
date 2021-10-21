package com.konkuk17.messenger_example.Chat

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

interface ChatService {

    //정의한 input을 넣어서 정의한 output을 호출하는 function.
    @FormUrlEncoded
    @POST("/chatting/chat_list_select/")
    fun selectChatList(     //여기가 input을 정의하는 곳. Field 안의 이름이 서버에서 post로 받는 input 이름과 똑같아야한다.
        @Field("user_id") user_id : String
    ): Call<List<Chatting>>       //output을 정의하는 곳.

    //정의한 input을 넣어서 정의한 output을 호출하는 function.
    @FormUrlEncoded
    @POST("/chatting/chat_list_insert/")
    fun insertChatList(     //여기가 input을 정의하는 곳. Field 안의 이름이 서버에서 post로 받는 input 이름과 똑같아야한다.
        @Field("user_id") user_id : String,
        @Field("friend_id") friend_id : String
    ): Call<List<Chatting>>       //output을 정의하는 곳.
}
