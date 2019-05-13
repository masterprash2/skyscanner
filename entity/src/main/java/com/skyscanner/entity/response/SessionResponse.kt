package com.skyscanner.entity.response


import com.skyscanner.challenge.entity.network.search.ServiceQuery
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionResponse(
    @Json(name = "ValidationErrors")
    val validationErrors: List<ValidationError>?,
    @Json(name = "ServiceQuery")
    val serviceQuery: ServiceQuery?
)