package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlightResponse(
    @Json(name = "Agents")
    val agents: List<Agent>,
    @Json(name = "Carriers")
    val carriers: List<Carrier>,
    @Json(name = "Currencies")
    val currencies: List<Currency>,
    @Json(name = "Itineraries")
    val itineraries: List<Itinerary>,
    @Json(name = "Legs")
    val legs: List<Leg>,
    @Json(name = "Places")
    val places: List<Place>,
    @Json(name = "Query")
    val query: Query,
    @Json(name = "Segments")
    val segments: List<Segment>,
    @Json(name = "ServiceQuery")
    val serviceQuery: ServiceQuery,
    @Json(name = "SessionKey")
    val sessionKey: String,
    @Json(name = "Status")
    val status: String
) {



}

