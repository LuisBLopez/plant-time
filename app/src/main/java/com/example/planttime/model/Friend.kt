package com.example.planttime.model

data class Friend(
    var name: String = "",
    var email: String =""
){
    override fun toString(): String {
        return "$name: $email"
    }
}
