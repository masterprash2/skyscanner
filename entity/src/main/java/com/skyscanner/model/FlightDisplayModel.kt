package com.skyscanner.model

import com.skyscanner.challenge.screen.results.model.item.ItineraryModel

data class FlightDisplayModel(
    val itineraries: List<ItineraryModel>,
    val areUpdatesPending: Boolean
)