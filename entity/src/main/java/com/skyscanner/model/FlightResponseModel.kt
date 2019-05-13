package com.skyscanner.model

import android.util.SparseArray
import com.skyscanner.challenge.entity.network.search.*
import com.skyscanner.challenge.entity.network.search.Currency
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class FlightResponseModel(
    val sessionUrl: String,
    var itineraries: List<Itinerary>,
    val agents: SparseArray<Agent>,
    val carriers: SparseArray<Carrier>,
    val currencies: Map<String, Currency>,
    val legs: Map<String, Leg>,
    val places: SparseArray<Place>,
    val segments: SparseArray<Segment>,
    var query: Query,
    var responseTimeZone: TimeZone = TimeZone.getTimeZone("en-GB")
) {


    fun fillFlightSearchResult(flightResponse: FlightResponse) {
        agentsListToMap(flightResponse)
        carriersToMap(flightResponse)
        currencyToMap(flightResponse)
        legsToMap(flightResponse)
        placesToMap(flightResponse)
        segmentsToMap(flightResponse)
        itineraries = flightResponse.itineraries
        query = flightResponse.query
    }

    private fun agentsListToMap(flightResponse: FlightResponse) {
        val map = agents
        for (agent in flightResponse.agents) {
            map.put(agent.id, agent)
        }
    }

    private fun carriersToMap(flightResponse: FlightResponse) {
        val map = carriers
        for (carrier in flightResponse.carriers) {
            map.put(carrier.id, carrier)
        }
    }

    private fun currencyToMap(flightResponse: FlightResponse) {
        val map = currencies as MutableMap
        for (currency in flightResponse.currencies) {
            map.put(currency.code, currency)
        }
    }

    private fun legsToMap(flightResponse: FlightResponse) {
        val map = legs as MutableMap
        for (leg in flightResponse.legs) {
            map.put(leg.id, leg)
        }
    }

    private fun placesToMap(flightResponse: FlightResponse) {
        val map = places
        for (place in flightResponse.places) {
            map.put(place.id, place)
        }
    }

    private fun segmentsToMap(flightResponse: FlightResponse) {
        val map = segments
        for (segment in flightResponse.segments) {
            map.put(segment.id, segment)
        }
    }


    companion object {
        fun createFrom(sessionUrl: String, flightResponse: FlightResponse) : FlightResponseModel {
            val flightResponseModel = FlightResponseModel(
                sessionUrl = sessionUrl,
                query = flightResponse.query,
                itineraries = ArrayList(),
                segments = SparseArray(),
                places = SparseArray(),
                legs = HashMap(),
                currencies = HashMap(),
                carriers = SparseArray(),
                agents = SparseArray()
            )
            flightResponseModel.fillFlightSearchResult(flightResponse)
            return flightResponseModel
        }

    }


}
