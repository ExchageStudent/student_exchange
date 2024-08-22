package com.example.student_exchange.model

data class Schedule(
    val scheduleName: String,
    val description: String,
    val allDay: Boolean,
    val startDate: String,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val repeatOption: String
)

data class ScheduleShow(
    val scheduleName: String,
    val startTime: String,
)