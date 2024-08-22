package com.example.student_exchange.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "travel_post")
//@TypeConverters(Converters::class)
data class TravelDto(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val pageView: Int,
    val likes: Int,
    val createdAt: String,
    val updatedAt: String ,
    val tags: List<TravelTagDto>? // This will now use the TypeConverter
)
