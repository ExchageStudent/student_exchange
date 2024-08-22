package com.example.student_exchange.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.student_exchange.repository.TravelRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelViewModel(private val travelRepository: TravelRepository) : ViewModel() {

    private val TAG = "TravelViewModel" // Define a TAG for logging

    fun createTravelPost(travelDto: TravelDto, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.d(TAG, "Creating travel post with title: ${traavelDto.title}")
        travelRepository.createTravelPost(travelDto).enqueue(object : Callback<TravelDto> {
            override fun onResponse(call: Call<TravelDto>, response: Response<TravelDto>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Travel post created successfully: ${response.body()}")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to create travel post. Error code: ${response.code()}, Message: $errorBody")
                    onError(errorBody)
                }
            }

            override fun onFailure(call: Call<TravelDto>, t: Throwable) {
                Log.e(TAG, "Network error while creating travel post: ${t.message}", t)
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun updateTravelPost(travelId: Long, travelDto: TravelDto, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.d(TAG, "Updating travel post with ID: $travelId")
        travelRepository.updateTravelPost(travelId, travelDto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Travel post updated successfully")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update travel post. Error code: ${response.code()}, Message: $errorBody")
                    onError(errorBody)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Network error while updating travel post: ${t.message}", t)
                onError(t.message ?: "Unknown error")
            }
        })
    }

    class Factory(private val travelRepository: TravelRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
                return TravelViewModel(travelRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
