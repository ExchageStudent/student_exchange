package com.example.student_exchange.model

data class MyPostsItem(
    val tag : String,
    val title: String,
    val detail: String,
    val imageUrls: List<String>,
    val viewCount: Int,
    val scrapCount: Int,
    var isSaved: Boolean = false,
    var isFinished: Boolean = false
)
