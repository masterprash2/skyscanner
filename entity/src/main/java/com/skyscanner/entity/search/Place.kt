package com.skyscanner.challenge.entity.network.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Place(
    @Json(name = "Code")
    val code: String,
    @Json(name = "Id")
    val id: Int,
    @Json(name = "Name")
    val name: String,
    @Json(name = "ParentId")
    val parentId: Int?,
    @Json(name = "Type")
    val type: String
)