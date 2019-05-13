package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Currency(
    @Json(name = "Code")
    val code: String,
    @Json(name = "DecimalDigits")
    val decimalDigits: Int,
    @Json(name = "DecimalSeparator")
    val decimalSeparator: String,
    @Json(name = "RoundingCoefficient")
    val roundingCoefficient: Int,
    @Json(name = "SpaceBetweenAmountAndSymbol")
    val spaceBetweenAmountAndSymbol: Boolean,
    @Json(name = "Symbol")
    val symbol: String,
    @Json(name = "SymbolOnLeft")
    val symbolOnLeft: Boolean,
    @Json(name = "ThousandsSeparator")
    val thousandsSeparator: String
)