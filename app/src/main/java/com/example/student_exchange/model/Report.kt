package com.example.student_exchange.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    var subjectTitle: String = "",
    var subjectReview: String = "",
    var subjectPhotos: List<Uri> = emptyList(),
    var etcTitle: String = "",
    var etcReview: String = "",
    var etcPhotos: List<Uri> = emptyList(),
    var gainReview: String = "",
    var merits: List<String> = emptyList(),
    var dismerits: List<String> = emptyList()
) : Parcelable