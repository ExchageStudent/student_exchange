package com.example.student_exchange.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TravelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(travelDto: TravelDto): Long

    @Update
    suspend fun update(travelDto: TravelDto)

    @Query("DELETE FROM travel_post WHERE id = :id")
    suspend fun deleteTravelPostById(id: Long): Int

    @Query("SELECT * FROM travel_post WHERE id = :id")
    fun getTravelPostById(id: Long): LiveData<TravelDto>
}

