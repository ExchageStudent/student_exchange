package com.example.student_exchange.repository

import androidx.lifecycle.LiveData
import com.example.student_exchange.TravelApiService
import com.example.student_exchange.model.TravelDao
import com.example.student_exchange.model.TravelDto
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelRepository(
    private val travelApiService: TravelApiService,
    private val travelDao: TravelDao
) {

    // Network methods
    suspend fun createTravelPost(travelDto: TravelDto): Response<TravelDto> {
        return withContext(Dispatchers.IO) {
            travelApiService.createTravelPost(travelDto).execute() // Synchronous call
        }
    }

    suspend fun updateTravelPost(travelId: Long, travelDto: TravelDto): Response<TravelDto> {
        return withContext(Dispatchers.IO) {
            travelApiService.updateTravelPost(travelId, travelDto).execute() // Synchronous call
        }
    }

    // Local DB methods
    suspend fun insertTravelPost(travelDto: TravelDto): Long {
        return travelDao.insert(travelDto)
    }

    suspend fun updateTravelPost(travelDto: TravelDto) {
        travelDao.update(travelDto)
    }

    fun getTravelPostById(id: Long): LiveData<TravelDto> {
        return travelDao.getTravelPostById(id)
    }

    suspend fun deleteTravelPostById(id: Long) {
        travelDao.deleteTravelPostById(id)
    }
}
