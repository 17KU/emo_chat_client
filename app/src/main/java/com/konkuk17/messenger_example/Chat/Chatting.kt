package com.konkuk17.messenger_example.Chat

//output을 만든다. 서버로 부터 받아오는 응답값.

data class Chatting(
    var chat_index : String,
    var chat_title : String,
    var chat_other_id : String)