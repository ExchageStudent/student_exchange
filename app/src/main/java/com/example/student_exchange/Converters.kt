package com.example.student_exchange.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Converters {
    private val gson = Gson()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }
    @TypeConverter
    fun fromTagsList(tags: List<TravelTagDto>?): String? {
        return tags?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toTagsList(tagsString: String?): List<TravelTagDto>? {
        return tagsString?.let {
            val listType = object : TypeToken<List<TravelTagDto>>() {}.type
            gson.fromJson(it, listType)
        }
    }
}
