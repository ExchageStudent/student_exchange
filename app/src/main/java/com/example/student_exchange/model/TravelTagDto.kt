package com.example.student_exchange.model

import java.time.LocalDateTime
import javax.security.auth.Subject

data class TravelTagDto(
    val id: Long,
    val travelPostId: Long,
    val subject: UserSubject, // Replace Subject with UserSubject
    val travelDateStart: String,
    val travelDateEnd:String,
    val countryName: String,
    val placeName: String
)

