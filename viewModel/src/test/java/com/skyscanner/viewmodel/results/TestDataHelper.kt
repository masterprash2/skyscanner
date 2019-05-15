package com.skyscanner.viewmodel.results

import com.skyscanner.challenge.entity.network.request.CabinClass
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.challenge.entity.network.search.*
import com.skyscanner.challenge.entity.network.search.Currency
import java.util.*

class TestDataHelper {
    companion object {

        fun dummyFlightQuery(): FlightQuery {
            val calendar = Calendar.getInstance(Locale.forLanguageTag("en-GB"));
            var advanceDays = 0;
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> advanceDays = 1
                Calendar.MONDAY -> advanceDays = 7
                Calendar.TUESDAY -> advanceDays = 6
                Calendar.WEDNESDAY -> advanceDays = 5
                Calendar.THURSDAY -> advanceDays = 4
                Calendar.FRIDAY -> advanceDays = 3
                Calendar.SATURDAY -> advanceDays = 2
            }
            calendar.add(Calendar.DATE, advanceDays)
            val dateOut = calendar.time
            calendar.add(Calendar.DATE, 1)
            val dateIn = calendar.time
            return FlightQuery(
                country = "UK",
                currency = "GBP",
                adults = 1,
                destinationPlace = "LOND-sky",
                originPlace = "EDI-sky",
                locationSchema = "sky",
                inboundDate = dateIn,
                outboundDate = dateOut,
                locale = "en-GB",
                cabinClass = CabinClass.ECONOMY
            )
        }

        fun dummyFlightResponse(): FlightResponse {
            return FlightResponse(
                agents = Arrays.asList(createAgent(5)),
                status = "UpdatesComplete",
                carriers = Arrays.asList(createCarrier(5)),
                query = createQuery(),
                sessionKey = "sessionkey",
                serviceQuery = ServiceQuery(""),
                segments = Arrays.asList(createSegment(5)),
                places = Arrays.asList(createPlace(5)),
                legs = Arrays.asList(createLeg(5)),
                itineraries = Arrays.asList(createIntineray(5)),
                currencies = Arrays.asList(currency(5))
            )
        }

        private fun currency(id: Int): Currency {
            return Currency(
                code = "GBP",
                decimalDigits = 1,
                decimalSeparator = "sep",
                roundingCoefficient = 1,
                spaceBetweenAmountAndSymbol = false,
                symbol = "Sym",
                symbolOnLeft = false,
                thousandsSeparator = "thou"
            )
        }

        private fun createIntineray(id: Int): Itinerary {
            return Itinerary(
                inboundLegId = id.toString(),
                bookingDetailsLink = BookingDetailsLink("", "", ""),
                outboundLegId = id.toString(),
                pricingOptions = Arrays.asList(createPricingOption(id))
            )
        }

        private fun createPricingOption(id: Int): PricingOption {
            return PricingOption(
                agents = Arrays.asList(id),
                deeplinkUrl = "deeplink",
                price = 1.0,
                quoteAgeInMinutes = 1
            )
        }

        private fun createLeg(id:Int): Leg {
            return Leg(
                arrival = "2019-05-13T10:45:00",
                id = id.toString(),
                originStation = id,
                journeyMode = "JourneyMode",
                duration = 1,
                directionality = "Direction",
                destinationStation = id,
                carriers = Arrays.asList(id),
                stops = Arrays.asList(id),
                segmentIds = Arrays.asList(id),
                operatingCarriers = Arrays.asList(id),
                flightNumbers = Arrays.asList(FlightNumber(id, "FlightNum")),
                departure = "2019-05-13T09:10:00"
            )
        }

        private fun createPlace(id: Int): Place {
            return Place(
                id = id,
                name = "PName"+id,
                type = "PType"+id,
                parentId = null,
                code = "Pcode"+id
            )
        }

        private fun createSegment(id: Int): Segment {
            return Segment(
                arrivalDateTime = "2019-04-13T19:15:00",
                id = id,
                destinationStation = id,
                directionality = "DirectionSeg",
                duration = 1232,
                journeyMode = "Jmode",
                originStation = id,
                operatingCarrier = id,
                flightNumber = "FlighNumSeg",
                carrier = id,
                departureDateTime = "2019-04-13T17:35:00"
            )
        }

        private fun createQuery(): Query {
            return Query(
                adults = 0,
                originPlace = "",
                locationSchema = "",
                locale = "",
                infants = 0,
                outboundDate = "",
                inboundDate = "",
                groupPricing = false,
                destinationPlace = "",
                currency = "GBP",
                country = "",
                children = 0,
                cabinClass = ""
            )
        }

        private fun createCarrier(id: Int): Carrier {
            return Carrier(
                code = "Code"+id,
                imageUrl = "",
                name = "Cname"+id,
                id = id,
                displayCode = "Disp"+id
            )
        }

        private fun createAgent(id: Int): Agent {
            return Agent(
                id = id,
                type = "ATYpe"+id,
                name = "Aname"+id,
                status = "Astat"+id,
                imageUrl = "",
                optimisedForMobile = false
            )
        }

    }


}