package com.konkuk17.messenger_example.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    //정의한 input을 넣어서 정의한 output을 호출하는 function.
    @FormUrlEncoded
    @POST("/login_signup/user_login/")
    fun requestLogin(
        //여기가 input을 정의하는 곳. Field 안의 이름이 서버에서 post로 받는 input 이름과 똑같아야한다.
        @Field("user_id") user_id : String,
        @Field("user_pw") user_pw : String
    ) : Call<Login>     //output을 정의하는 곳.
}