package com.example.student_exchange.retrofit

data class SchedulesRetrofit(
    val endTime: String,
    val recurrence: Recurrence,
    val scheduleDescription: String,
    val scheduleName: String,
    val startTime: String,
    val tagNames: List<String>,
    val userId: Int
) {
    data class Recurrence(
        val daysOfWeek: List<String>,
        val recurrenceEndDate: String,
        val recurrenceInterval: Int,
        val type: String
    )
}