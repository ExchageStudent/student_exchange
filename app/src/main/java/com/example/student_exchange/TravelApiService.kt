package com.example.student_exchange

import com.example.student_exchange.model.TravelDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TravelApiService {

    @GET("/api/travel")
    fun getAllTravelPosts(): Call<List<TravelDto>>

    @GET("/api/travel/{id}")
    fun getTravelPostById(@Path("id") id: Long): Call<TravelDto>

    @POST("/api/travel/post")
    fun createTravelPost(@Body travelDto: TravelDto): Call<TravelDto>

    @PUT("/api/travel/{id}")
    fun updateTravelPost(@Path("id") id: Long, @Body travelDto: TravelDto): Call<TravelDto>

    @DELETE("/api/travel/{id}")
    fun deleteTravelPost(@Path("id") id: Long): Call<Void>

    @POST("/api/travel/{id}/like")
    fun likeTravelPost(@Path("id") id: Long): Call<Void>

    @POST("/api/travel/{id}/unlike")
    fun unlikeTravelPost(@Path("id") id: Long): Call<Void>
}
