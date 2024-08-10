package com.example.student_exchange.model

data class MyPostsItem(
    val title: String,
    val subtitle: String,
    val imageUrls: List<String>,
    val viewCount: Int,
    val scrapCount: Int
)
