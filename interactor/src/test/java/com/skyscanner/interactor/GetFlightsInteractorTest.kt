package com.skyscanner.interactor

import com.nhaarman.mockitokotlin2.whenever
import com.skyscanner.challenge.entity.network.search.*
import com.skyscanner.challenge.entity.network.search.Currency
import com.skyscanner.entity.response.Response
import com.skyscanner.interactor.utils.TransformationUtils
import com.skyscanner.repository.FlightRepository
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.model.FlightDisplayModel
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class GetFlightsInteractorTest {

    lateinit var searchInteractor: GetFlightsInteractor
    lateinit var flightRepository: FlightRepository
    lateinit var platformUtils: PlatformUtils
    lateinit var transformationUtils: TransformationUtils

    @Before
    fun setUp() {
        flightRepository = Mockito.mock(FlightRepository::class.java)
        platformUtils = Mockito.mock(PlatformUtils::class.java)
        transformationUtils = TransformationUtils(platformUtils)
        searchInteractor = GetFlightsInteractor(flightRepository,transformationUtils)
    }

    @Test
    fun testSuccess() {
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl)).thenReturn(Observable.just(createDummyResponse()))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertNoErrors()
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first();
        assertTrue(response.success)
        assertEquals(1, response.value?.itineraries?.size)
    }


    @Test
    fun testFailure() {
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl)).thenReturn(Observable.just(createErrorResponse()))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertNoErrors()
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first();
        assertFalse(response.success)
        assertNotNull(response.exception)
    }


    @Test
    fun testForNoObservableError() {
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl)).thenReturn(Observable.error(NullPointerException()))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertNoErrors()
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first();
        assertFalse(response.success)
        assertNotNull(response.exception)
    }

    @Test
    fun testEmptyResponseButNetworkSuccess() {
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl)).thenReturn(Observable.just(Response<FlightResponse>(null,true)))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertNoErrors()
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first();
        assertFalse(response.success)
    }


    private fun createErrorResponse(): Response<FlightResponse> {
        return Response<FlightResponse>(value = null, success = false).apply { exception = NullPointerException() }
    }

    private fun createDummyResponse(): Response<FlightResponse> {
        return Response(dummyFlightResponse(), true)
    }

    private fun dummyFlightResponse(): FlightResponse {
        return FlightResponse(
            agents = Arrays.asList(createAgent()),
            status = "UpdatePending",
            carriers = Arrays.asList(createCarrier()),
            query = createQuery(),
            sessionKey = "sessionkey",
            serviceQuery = ServiceQuery(""),
            segments = Arrays.asList(createSegment()),
            places = Arrays.asList(createPlace()),
            legs = Arrays.asList(createLeg()),
            itineraries = Arrays.asList(createIntineray()),
            currencies = Arrays.asList(currency())
        )
    }

    private fun currency(): Currency {
        return Currency(
            code = "",
            decimalDigits = 1,
            decimalSeparator = "",
            roundingCoefficient = 1,
            spaceBetweenAmountAndSymbol = false,
            symbol = "",
            symbolOnLeft = false,
            thousandsSeparator = ""
        )
    }

    private fun createIntineray(): Itinerary {
        return Itinerary(
            inboundLegId = "1",
            bookingDetailsLink = BookingDetailsLink("", "", ""),
            outboundLegId = "1",
            pricingOptions = Arrays.asList(createPricingOption())
        )
    }

    private fun createPricingOption(): PricingOption {
        return PricingOption(
            agents = Arrays.asList(1),
            deeplinkUrl = "",
            price = 1.0,
            quoteAgeInMinutes = 1
        )
    }

    private fun createLeg(): Leg {
        return Leg(
            arrival = "",
            id = "1",
            originStation = 1,
            journeyMode = "",
            duration = 1,
            directionality = "",
            destinationStation = 1,
            carriers = Arrays.asList(1),
            stops = Arrays.asList(1),
            segmentIds = Arrays.asList(1),
            operatingCarriers = Arrays.asList(1),
            flightNumbers = Arrays.asList(FlightNumber(1, "")),
            departure = ""
        )
    }

    private fun createPlace(): Place {
        return Place(
            id = 1,
            name = "",
            type = "",
            parentId = null,
            code = ""
        )
    }

    private fun createSegment(): Segment {
        return Segment(
            arrivalDateTime = "",
            id = 1,
            destinationStation = 1,
            directionality = "",
            duration = 1,
            journeyMode = "",
            originStation = 1,
            operatingCarrier = 1,
            flightNumber = "",
            carrier = 1,
            departureDateTime = ""
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
            currency = "",
            country = "",
            children = 0,
            cabinClass = ""
        )
    }

    private fun createCarrier(): Carrier {
        return Carrier(
            code = "",
            imageUrl = "",
            name = "",
            id = 1,
            displayCode = ""
        )
    }

    private fun createAgent(): Agent {
        return Agent(
            id = 1,
            type = "",
            name = "",
            status = "",
            imageUrl = "",
            optimisedForMobile = false
        )
    }

    @After
    fun tearDown() {
    }
}