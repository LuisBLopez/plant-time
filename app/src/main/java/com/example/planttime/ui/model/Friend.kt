package com.example.planttime.ui.model

data class Friend(
    var nickname: String = "",
    var email: String = ""
){
    override fun toString(): String {
        return "$nickname: $email"
    }
}
