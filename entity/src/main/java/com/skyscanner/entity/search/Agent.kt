package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Agent(
    @Json(name = "Id")
    val id: Int,
    @Json(name = "ImageUrl")
    val imageUrl: String,
    @Json(name = "Name")
    val name: String,
    @Json(name = "OptimisedForMobile")
    val optimisedForMobile: Boolean,
    @Json(name = "Status")
    val status: String,
    @Json(name = "Type")
    val type: String
)