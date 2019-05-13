package com.skyscanner.model

import com.skyscanner.entity.response.SessionResponse

data class SessionModel(
    val sessionUrl: String?,
    val response: SessionResponse
)