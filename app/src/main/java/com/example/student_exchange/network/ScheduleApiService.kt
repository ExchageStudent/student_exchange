package com.example.student_exchange.network

import com.example.student_exchange.model.Schedule
import com.example.student_exchange.model.ScheduleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleApiService {
    @POST("/api/schedules")
    fun saveSchedule(@Body scheduleRequest: ScheduleRequest): Call<Map<String, Int>>

    @GET("/api/schedules/{scheduleId}")
    fun getSchedule(@Path("scheduleId") scheduleId: Int): Call<Schedule>
}