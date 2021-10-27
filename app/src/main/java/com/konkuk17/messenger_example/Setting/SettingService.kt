package com.konkuk17.messenger_example.Setting

import com.konkuk17.messenger_example.Signup.MyResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SettingService {

    @FormUrlEncoded
    @POST("/login_signup/user_name_change/")
    fun requestNameChange(
        @Field("user_id") user_id : String,
        @Field("user_pw") user_pw : String,
        @Field("user_name") user_name : String
    ) : Call<MyResponse>

    @FormUrlEncoded
    @POST("/login_signup/user_pwd_change/")
    fun requestPwdChange(
        @Field("user_id") user_id : String,
        @Field("user_pw") user_pw : String,
        @Field("user_new_pw") user_new_pw : String
    ) : Call<MyResponse>
}
