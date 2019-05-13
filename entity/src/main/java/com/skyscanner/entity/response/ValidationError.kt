package com.skyscanner.entity.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidationError(
    @Json(name = "Message")
    val message: String,
    @Json(name = "ParameterName")
    val parameterName: String
)