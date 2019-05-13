package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Query(
    @Json(name = "Adults")
    val adults: Int,
    @Json(name = "CabinClass")
    val cabinClass: String,
    @Json(name = "Children")
    val children: Int,
    @Json(name = "Country")
    val country: String,
    @Json(name = "Currency")
    val currency: String,
    @Json(name = "DestinationPlace")
    val destinationPlace: String,
    @Json(name = "GroupPricing")
    val groupPricing: Boolean,
    @Json(name = "InboundDate")
    val inboundDate: String,
    @Json(name = "Infants")
    val infants: Int,
    @Json(name = "Locale")
    val locale: String,
    @Json(name = "LocationSchema")
    val locationSchema: String,
    @Json(name = "OriginPlace")
    val originPlace: String,
    @Json(name = "OutboundDate")
    val outboundDate: String
)