package com.konkuk17.messenger_example.ChatRoom

data class ChatModel (
    //채팅방의 유저들
    var users : MutableMap<String, Boolean> = HashMap<String, Boolean>(),
    // 채팅방의 내용
    var comments : MutableMap<String, Comment> = HashMap<String, Comment>()){

    inner class Comment(
        var uid : String?,
        var message : String?
    )
}