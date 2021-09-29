package com.konkuk17.messenger_example.Friends
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FriendService {

    @FormUrlEncoded
    @POST("/friends/add_friend/")
    fun AddFriend(
        @Field("user_id") user_id : String,
        @Field("add_friend_id") add_friend_id : String
        ) : Call<AddFriendOutput>

    @FormUrlEncoded
    @POST("/friends/show_friend/")
    fun ShowFriend(
        @Field("user_id") user_id : String
        ) : Call<List<ShowFriendOutput>>

    @FormUrlEncoded
    @POST("/friends/add_favorite")
    fun AddFavorite(
        @Field("user_id") user_id : String,
        @Field("favorite_add") favorite_add : String
    ) : Call<AddFriendOutput>
}