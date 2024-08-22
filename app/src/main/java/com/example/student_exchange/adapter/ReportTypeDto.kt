package com.example.student_exchange.adapter

data class ReportResponse(
    val status: String,
    val data: List<ReportItem>
)

data class ReportItem(
    val id: Int,
    val title: String,
    val content: String,
    val date: String
    // Add other fields as necessary
)

data class ReportTypeDto(
    val reportId: Long,
    val reportType: String
)