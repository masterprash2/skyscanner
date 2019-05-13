package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PricingOption(
    @Json(name = "Agents")
    val agents: List<Int>,
    @Json(name = "DeeplinkUrl")
    val deeplinkUrl: String,
    @Json(name = "Price")
    val price: Double,
    @Json(name = "QuoteAgeInMinutes")
    val quoteAgeInMinutes: Int
)