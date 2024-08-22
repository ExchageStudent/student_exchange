package com.example.student_exchange.network

import com.example.student_exchange.api.ReportApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://3.36.145.58:8080") // 여기에 실제 서버의 URL을 입력하세요.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val scheduleApi: ScheduleApiService by lazy {
        retrofit.create(ScheduleApiService::class.java)
    }

    val reportApi: ReportApiService by lazy {
        retrofit.create(ReportApiService::class.java)
    }
}