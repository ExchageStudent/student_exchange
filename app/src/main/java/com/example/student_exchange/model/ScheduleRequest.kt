package com.example.student_exchange.model

data class Recurrence(
    val type: String,
    val daysOfWeek: List<String>,
    val recurrenceInterval: Int,
    val recurrenceEndDate: String
)

data class ScheduleRequest(
    val userId: Int,
    // val scheduleId: Int = 0, // 서버가 scheduleId를 요구하므로 기본값 0을 제공
    val scheduleName: String,
    val scheduleDescription: String,
    val startTime: String,
    val endTime: String,
    val tagNames: List<String>,
    // val recurrence: Recurrence?
)
