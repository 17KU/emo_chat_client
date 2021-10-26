package com.konkuk17.messenger_example.ChatRoom

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface EmoService {

    @FormUrlEncoded
    @POST("/KoBert/get_emotion")
    fun GetEmotion(
        @Field("message") message : String
    ) : Call<EmoOutput>
}