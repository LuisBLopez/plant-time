package com.example.planttime.ui.model

data class Friend(
    var name: String = "",
    var email: String =""
){
    override fun toString(): String {
        return "$name: $email"
    }
}
