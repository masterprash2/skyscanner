package com.skyscanner.challenge.screen.results.model.item

data class ItineraryModel(
    val inboundFlight : DirectionModel,
    val outboundFlight : DirectionModel,
    val agent: String,
    val rating: String,
    val ratingImage : String,
    val price : String
)