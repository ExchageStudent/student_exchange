package com.example.student_exchange.model

data class PostItem(
    val title: String,
    val details: String,
    val author: String,
    val date: String,
    val views: Int,
    val scrap: Int,
    var isSaved: Boolean = false,
    var isFinished: Boolean = false,
    var topic: String
)
