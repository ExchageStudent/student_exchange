package com.example.student_exchange.adapter

data class StageDto(
    val reportId: Long,
    val stageOrder: Int
)

data class StageResponseDto(
    val content: String,
    val options: List<String>,
    val selectedOptions: List<Int>,
    val imageUrls: List<Base64Image>
)

data class ReportStageDto (
    val reportId: Long,
    val stageOrder: Int,
    val optionOrders: List<Int>,
    val base64Images: List<Base64Image>
)

data class Base64Image(
    val base64Image: String,
    val originalFilename: String
)
