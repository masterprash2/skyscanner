package com.skyscanner.interactor

import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.challenge.screen.results.model.item.DirectionModel
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel
import com.squareup.moshi.Moshi
import okio.BufferedSource
import okio.Okio

fun createItinerary1(): ItineraryModel {

    val inboundDirection = DirectionModel(
        name = "ZRH-AT, Aer Lingus",
        stops = "Direct",
        imageUrl = "imageUrl2",
        duration = "5h 50m",
        timeFrame = "00:20 - 13:50"
    );

    val outbound = DirectionModel(
        name = "ZRH-ZRH, Flybe",
        stops = "Direct",
        imageUrl = "imageUrl1",
        duration = "3h 20m",
        timeFrame = "09:20 - 11:50"
    );
    return createItineraryModel(
        inbound = inboundDirection,
        outbound = outbound,
        price = "£27.0",
        agent = "via eurowings"
    )
}


fun createItinerary2(): ItineraryModel {

    val inboundDirection = DirectionModel(
        name = "AT-NWI, Stobart Air",
        stops = "Direct",
        imageUrl = "imageUrl3",
        duration = "7h 30m",
        timeFrame = "14:20 - 18:50"
    );

    val outbound = DirectionModel(
        name = "ZRH-AT, Aer Lingus",
        stops = "Direct",
        imageUrl = "imageUrl2",
        duration = "5h 50m",
        timeFrame = "00:20 - 13:50"
    );
    return createItineraryModel(
        inbound = inboundDirection,
        outbound = outbound,
        price = "£27.0",
        agent = "via Flybe"
    )
}

fun createItinerary3(): ItineraryModel {

    val inboundDirection = DirectionModel(
        name = "ZRH-NWI, Stobart Air",
        stops = "Direct",
        imageUrl = "imageUrl4",
        duration = "1h 30m",
        timeFrame = "19:20 - 23:50"
    );

    val outbound = DirectionModel(
        name = "AT-NWI, Stobart Air",
        stops = "Direct",
        imageUrl = "imageUrl3",
        duration = "7h 30m",
        timeFrame = "14:20 - 18:50"
    );
    return createItineraryModel(
        inbound = inboundDirection,
        outbound = outbound,
        price = "£347.0",
        agent = "via Flybe"
    )
}


fun createItineraryModel(
    price: String, agent: String,
    outbound: DirectionModel, inbound: DirectionModel
): ItineraryModel {
    return ItineraryModel(
        ratingImage = "",
        rating = "10.0",
        outboundFlight = outbound,
        inboundFlight = inbound,
        agent = agent,
        price = price
    )
}

val FILE_FLIGHT_RESPONSE_VALID = "valid_results.json"
val FILE_FLIGHT_RESPONSE_INVALID = "invalid_results.json"

fun resourceFileToStream(classLoader: ClassLoader ,fileName: String): BufferedSource {
    return Okio.buffer(Okio.source(classLoader.getResourceAsStream(fileName)))
}

fun flightResponse(fileName: String) : FlightResponse {
    val moshi = Moshi.Builder().build()
    val adapter = moshi.adapter(FlightResponse::class.java)
    return  adapter.fromJson(resourceFileToStream(moshi.javaClass.classLoader,fileName))!!
}
