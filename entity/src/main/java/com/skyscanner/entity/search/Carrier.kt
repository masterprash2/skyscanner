package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Carrier(
    @Json(name = "Code")
    val code: String,
    @Json(name = "DisplayCode")
    val displayCode: String,
    @Json(name = "Id")
    val id: Int,
    @Json(name = "ImageUrl")
    val imageUrl: String,
    @Json(name = "Name")
    val name: String
)