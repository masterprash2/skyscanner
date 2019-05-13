package com.skyscanner.entity.response

data class Response<T>(
    val value: T?,
    val success: Boolean
) {

    var exception: Throwable? = null

}