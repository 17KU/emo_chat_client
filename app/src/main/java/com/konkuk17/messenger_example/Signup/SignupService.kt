package com.konkuk17.messenger_example.Signup
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface SignupService {
    //정의한 input을 넣어서 정의한 output을 호출하는 function.
    @FormUrlEncoded
    @POST("/login_signup/user_regist")
    fun requestSingup(
        //여기가 input을 정의하는 곳. Field 안의 이름이 서버에서 post로 받는 input 이름과 똑같아야한다.
        @Field("user_id") user_id : String,
        @Field("user_pw") user_pw : String,
        @Field("user_name") user_name : String
    ) : Call<MyResponse> //output을 정의하는 곳.

    @FormUrlEncoded
    @POST("/login_signup/user_dupli_check")
    fun requestCheck(
        @Field("user_id") user_id : String
    ) : Call<MyResponse>
}
