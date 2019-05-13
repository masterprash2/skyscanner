package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookingDetailsLink(
    @Json(name = "Body")
    val body: String,
    @Json(name = "Method")
    val method: String,
    @Json(name = "Uri")
    val uri: String
)