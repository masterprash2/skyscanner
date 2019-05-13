package com.skyscanner.entity.request

import com.skyscanner.challenge.entity.network.request.CabinClass
import java.util.*
import kotlin.collections.HashMap

data class FlightQuery(
    val country: String,
    val currency: String,
    val locale: String,
    val originPlace: String,
    val destinationPlace: String,
    val outboundDate: Date,
    val inboundDate: Date? = null,
    val cabinClass: CabinClass? = null,
    val adults: Int,
    val children: Int = 0,
    val infants: Int = 0,
    val locationSchema: String
)