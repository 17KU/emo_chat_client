package com.konkuk17.messenger_example.Login

//output을 만든다. 서버로 부터 받아오는 응답값.
data class Login(
    var code : String,
    var msg : String,
    var user_id : String,
    var user_name : String
)