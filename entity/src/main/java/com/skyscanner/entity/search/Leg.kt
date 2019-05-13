package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Leg(
    @Json(name = "Arrival")
    val arrival: String,
    @Json(name = "Carriers")
    val carriers: List<Int>,
    @Json(name = "Departure")
    val departure: String,
    @Json(name = "DestinationStation")
    val destinationStation: Int,
    @Json(name = "Directionality")
    val directionality: String,
    @Json(name = "Duration")
    val duration: Int,
    @Json(name = "FlightNumbers")
    val flightNumbers: List<FlightNumber>,
    @Json(name = "Id")
    val id: String,
    @Json(name = "JourneyMode")
    val journeyMode: String,
    @Json(name = "OperatingCarriers")
    val operatingCarriers: List<Int>,
    @Json(name = "OriginStation")
    val originStation: Int,
    @Json(name = "SegmentIds")
    val segmentIds: List<Int>,
    @Json(name = "Stops")
    val stops: List<Int>
)