package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Itinerary(
    @Json(name = "BookingDetailsLink")
    val bookingDetailsLink: BookingDetailsLink,
    @Json(name = "InboundLegId")
    val inboundLegId: String,
    @Json(name = "OutboundLegId")
    val outboundLegId: String,
    @Json(name = "PricingOptions")
    val pricingOptions: List<PricingOption>
)