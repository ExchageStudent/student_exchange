package com.example.student_exchange.api

import com.example.student_exchange.adapter.ReportStageDto
import com.example.student_exchange.adapter.ReportTypeDto
import com.example.student_exchange.adapter.StageDto
import com.example.student_exchange.adapter.StageResponseDto
import com.example.student_exchange.model.ReportIdResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApiService {

    // 보고서 생성
    @POST("api/reports/{userId}")
    fun createReport(@Path("userId") userId: Int): Call<ResponseBody>

    // 보고서 유형
    @PATCH("api/reports/type")
    fun sendReportType(@Body reportTypeDto: ReportTypeDto): Call<ReportIdResponse>

    // 보고서 step 받아오기
    @POST("api/reports/stage")
    fun getReportStage(@Body stageDto: StageDto): Call<StageResponseDto>

    // step별 보고서 내용 넘겨주기
    @PUT("api/reports/stage")
    fun updateReportStage(@Body stageDto: ReportStageDto): Call<StageResponseDto>

    // 전체 일정 조회
    @GET("api/reports/report/{reportId}")
    fun getReport(@Path("reportId") reportId: Long): Call<List<StageResponseDto>>
}