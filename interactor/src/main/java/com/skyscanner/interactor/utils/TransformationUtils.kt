package com.skyscanner.interactor.utils

import com.skyscanner.challenge.entity.network.search.Itinerary
import com.skyscanner.challenge.entity.network.search.Leg
import com.skyscanner.challenge.screen.results.model.item.DirectionModel
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel
import com.skyscanner.model.FlightResponseModel
import com.skyscanner.repository.PlatformUtils
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TransformationUtils @Inject constructor(private val platformUtils: PlatformUtils) {

    fun transform(input: FlightResponseModel): List<ItineraryModel> {
        val list = ArrayList<ItineraryModel>(input.itineraries.size)
        for (itinerary in input.itineraries) {
            val mapItineraryToViewModel = mapItineraryToViewModel(itinerary, input);
            if(mapItineraryToViewModel!=null)
                list.add(mapItineraryToViewModel);
        }
        return list;
    }

    private fun mapItineraryToViewModel(itinerary: Itinerary, searchResult: FlightResponseModel): ItineraryModel? {
        val inboundLeg = searchResult.legs.get(itinerary.inboundLegId)
        val outboundLeg = searchResult.legs.get(itinerary.outboundLegId)
        val pricingOption = itinerary.pricingOptions.firstOrNull()
        val agent = searchResult.agents.get(pricingOption?.agents?.firstOrNull() ?: -1)
        val price = pricingOption?.price;
        val currency = searchResult.currencies.get(searchResult.query.currency)
        if(agent == null || currency == null || price == null) {
            return null
        }

        return ItineraryModel(
            agent = "via " + (agent?.name ?: ""),
            price = (currency?.symbol ?: "") + price.toString(),
            ratingImage = "",
            rating = "10.0",
            outboundFlight = mapLeg(outboundLeg, searchResult),
            inboundFlight = mapLeg(inboundLeg, searchResult)
        )
    }

    private fun mapLeg(leg: Leg?, searchResult: FlightResponseModel): DirectionModel {
        if (leg == null) {
            return emptyDirectionViewModel()
        } else {
            return  retrieveDirectionViewModel(leg, searchResult) ?: emptyDirectionViewModel()

        }
    }

    private fun retrieveDirectionViewModel(leg: Leg, searchResult: FlightResponseModel): DirectionModel? {
//        val segment = searchResult.segments.get(leg.segmentIds.first())
        val carrier = searchResult.carriers.get(leg.carriers.first())
        val stops = if (leg.stops.isEmpty()) "Direct" else (leg.stops.size.toString() + " Stops");
        val origin = searchResult.places.get(leg.originStation)
        val destination = searchResult.places.get(leg.destinationStation)
        if(carrier == null || origin == null || destination == null) {
            return null
        }
        return DirectionModel(
            duration = formatDuration(leg.duration),
            timeFrame = formatTimeFrame(leg.departure, leg.arrival, searchResult.query.locale,searchResult.responseTimeZone),
            imageUrl = carrier.imageUrl,
            stops = stops,
            name = String.format("%s-%s, %s", origin.code, destination.code, carrier.name)
        )
    }

    private fun emptyDirectionViewModel(): DirectionModel {
        return DirectionModel(
            name = "",
            timeFrame = "",
            duration = "",
            stops = "",
            imageUrl = ""
        );
    }

    private fun formatTimeFrame(departure: String, arrival: String, locale: String, timeZone: TimeZone): String {
        val departureDate = platformUtils.parseDate("yyyy-MM-dd'T'hh:mm:ss", departure, timeZone)
        val arrivalDate = platformUtils.parseDate("yyyy-MM-dd'T'hh:mm:ss", arrival, timeZone)
        return platformUtils.formatDate("HH:mm", departureDate,locale) + " - " + platformUtils.formatDate("HH:mm", arrivalDate,locale)
    }

    private fun formatDuration(duration: Int): String {
        val days = duration / (24 * 60);
        val hours = (duration % (24 * 60)) / 60
        val minutes = (duration % (24 * 60)) % 60
        val sb = StringBuilder()
        if (days > 0) {
            sb.append(days).append("d ")
        }
        if (hours > 0) {
            sb.append(hours).append("h ")
        }
        if (minutes > 0) {
            sb.append(minutes).append("m")
        }
        return sb.trim().toString()
    }

}