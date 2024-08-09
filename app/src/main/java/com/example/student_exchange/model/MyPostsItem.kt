package com.example.student_exchange.model

data class MyPostsItem(
    private String title;
    private String subtitle;
    private List<String> imageUrls;
    private int viewCount;
    private int likeCount;
)
