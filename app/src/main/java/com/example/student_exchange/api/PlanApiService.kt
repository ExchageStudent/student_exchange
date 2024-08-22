package com.example.student_exchange.api

import com.example.student_exchange.model.ScheduleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PlanApiService {
    @POST("api/schedules")
    fun saveSchedule(@Body scheduleRequest: ScheduleRequest): Call<Map<String, Int>>
}