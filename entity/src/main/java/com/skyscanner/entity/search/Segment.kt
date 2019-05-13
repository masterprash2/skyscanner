package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Segment(
    @Json(name = "ArrivalDateTime")
    val arrivalDateTime: String,
    @Json(name = "Carrier")
    val carrier: Int,
    @Json(name = "DepartureDateTime")
    val departureDateTime: String,
    @Json(name = "DestinationStation")
    val destinationStation: Int,
    @Json(name = "Directionality")
    val directionality: String,
    @Json(name = "Duration")
    val duration: Int,
    @Json(name = "FlightNumber")
    val flightNumber: String,
    @Json(name = "Id")
    val id: Int,
    @Json(name = "JourneyMode")
    val journeyMode: String,
    @Json(name = "OperatingCarrier")
    val operatingCarrier: Int,
    @Json(name = "OriginStation")
    val originStation: Int
)