package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlightNumber(
    @Json(name = "CarrierId")
    val carrierId: Int,
    @Json(name = "FlightNumber")
    val flightNumber: String
)