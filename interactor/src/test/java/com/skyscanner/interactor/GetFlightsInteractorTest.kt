package com.skyscanner.interactor

import com.nhaarman.mockitokotlin2.whenever
import com.skyscanner.challenge.entity.network.search.*
import com.skyscanner.challenge.entity.network.search.Currency
import com.skyscanner.entity.response.Response
import com.skyscanner.interactor.mock.MockPlatformImpl
import com.skyscanner.interactor.utils.TransformationUtils
import com.skyscanner.model.FlightDisplayModel
import com.skyscanner.repository.FlightRepository
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class GetFlightsInteractorTest {

    lateinit var searchInteractor: GetFlightsInteractor
    lateinit var flightRepository: FlightRepository
    lateinit var platformUtils: MockPlatformImpl
    lateinit var transformationUtils: TransformationUtils

    @Before
    fun setUp() {
        flightRepository = Mockito.mock(FlightRepository::class.java)
        platformUtils = MockPlatformImpl()
        transformationUtils = TransformationUtils(platformUtils)
        searchInteractor = GetFlightsInteractor(flightRepository, transformationUtils, platformUtils)
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
        assertEquals(3, response.value?.itineraries?.size)
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
        whenever(flightRepository.getFlights(sessionurl)).thenReturn(
            Observable.just(
                Response<FlightResponse>(
                    null,
                    true
                )
            )
        )
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertNoErrors()
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first();
        assertFalse(response.success)
    }

    @Test
    fun pollingOnUpdatesPending() {

        val responsePublisher = BehaviorSubject.create<Observable<Response<FlightResponse>>>()
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl))
            .thenReturn(responsePublisher.flatMap { it })


        var flightReponse = validResponse()

        //Repeat after updates pending
        responsePublisher.onNext(Observable.just(Response(flightReponse.copy(status = "UPDATESPENDING"), true)))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertValueCount(1)
        val first = observer.values().first()
        assertTrue(first.success)
        assertNotNull(first.value)
        assertTrue(first.value!!.areUpdatesPending)


        //Repeat after updates pending
        responsePublisher.onNext(Observable.just(Response(flightReponse.copy(status = "UPDATESPENDING"), true)))
        platformUtils.delayPublisher.onNext(0)
        observer.assertValueCount(2)
        val second = observer.values()[1]
        assertTrue(second.success)
        assertNotNull(second.value)
        assertTrue(second.value!!.areUpdatesPending)


        //Check updates complete
        responsePublisher.onNext(Observable.just(Response(flightReponse.copy(status = "UpdatesComplete"), true)))
        platformUtils.delayPublisher.onNext(0)
        observer.assertValueCount(3)
        val third = observer.values()[2]
        assertTrue(third.success)
        assertNotNull(third.value)
        assertFalse(third.value!!.areUpdatesPending)

        // Check no more repeats
        platformUtils.delayPublisher.onNext(0)
        observer.assertValueCount(3)
    }

    @Test
    fun dontPollInCaseOfFailure() {

        val responsePublisher = BehaviorSubject.create<Observable<Response<FlightResponse>>>()
        val sessionurl = "sessionUrl"
        whenever(flightRepository.getFlights(sessionurl))
            .thenReturn(responsePublisher.flatMap { it })


        //Repeat after updates pending
        responsePublisher.onNext(Observable.just(Response<FlightResponse>(null, false)))
        val observer = TestObserver<Response<FlightDisplayModel>>()
        searchInteractor.getFlights(sessionurl).subscribe(observer)
        observer.assertValueCount(1)

        platformUtils.delayPublisher.onNext(0)
        observer.assertValueCount(1)
    }


    private fun createErrorResponse(): Response<FlightResponse> {
        return Response<FlightResponse>(value = null, success = false).apply { exception = NullPointerException() }
    }

    private fun createDummyResponse(): Response<FlightResponse> {
        return Response(validResponse(), true)
    }


    private fun validResponse(): FlightResponse {
        return flightResponse(FILE_FLIGHT_RESPONSE_VALID)
    }


//    private fun dummyFlightResponse(): FlightResponse {
//        return FlightResponse(
//            agents = Arrays.asList(createAgent()),
//            status = "UpdatePending",
//            carriers = Arrays.asList(createCarrier()),
//            query = createQuery(),
//            sessionKey = "sessionkey",
//            serviceQuery = ServiceQuery(""),
//            segments = Arrays.asList(createSegment()),
//            places = Arrays.asList(createPlace()),
//            legs = Arrays.asList(createLeg()),
//            itineraries = Arrays.asList(createIntineray()),
//            currencies = Arrays.asList(currency())
//        )
//    }

//    private fun currency(): Currency {
//        return Currency(
//            code = "",
//            decimalDigits = 1,
//            decimalSeparator = "",
//            roundingCoefficient = 1,
//            spaceBetweenAmountAndSymbol = false,
//            symbol = "",
//            symbolOnLeft = false,
//            thousandsSeparator = ""
//        )
//    }
//
//    private fun createIntineray(): Itinerary {
//        return Itinerary(
//            inboundLegId = "1",
//            bookingDetailsLink = BookingDetailsLink("", "", ""),
//            outboundLegId = "1",
//            pricingOptions = Arrays.asList(createPricingOption())
//        )
//    }
//
//    private fun createPricingOption(): PricingOption {
//        return PricingOption(
//            agents = Arrays.asList(1),
//            deeplinkUrl = "",
//            price = 1.0,
//            quoteAgeInMinutes = 1
//        )
//    }
//
//    private fun createLeg(): Leg {
//        return Leg(
//            arrival = "",
//            id = "1",
//            originStation = 1,
//            journeyMode = "",
//            duration = 1,
//            directionality = "",
//            destinationStation = 1,
//            carriers = Arrays.asList(1),
//            stops = Arrays.asList(1),
//            segmentIds = Arrays.asList(1),
//            operatingCarriers = Arrays.asList(1),
//            flightNumbers = Arrays.asList(FlightNumber(1, "")),
//            departure = ""
//        )
//    }
//
//    private fun createPlace(): Place {
//        return Place(
//            id = 1,
//            name = "",
//            type = "",
//            parentId = null,
//            code = ""
//        )
//    }
//
//    private fun createSegment(): Segment {
//        return Segment(
//            arrivalDateTime = "",
//            id = 1,
//            destinationStation = 1,
//            directionality = "",
//            duration = 1,
//            journeyMode = "",
//            originStation = 1,
//            operatingCarrier = 1,
//            flightNumber = "",
//            carrier = 1,
//            departureDateTime = ""
//        )
//    }
//
//    private fun createQuery(): Query {
//        return Query(
//            adults = 0,
//            originPlace = "",
//            locationSchema = "",
//            locale = "",
//            infants = 0,
//            outboundDate = "",
//            inboundDate = "",
//            groupPricing = false,
//            destinationPlace = "",
//            currency = "",
//            country = "",
//            children = 0,
//            cabinClass = ""
//        )
//    }
//
//    private fun createCarrier(): Carrier {
//        return Carrier(
//            code = "",
//            imageUrl = "",
//            name = "",
//            id = 1,
//            displayCode = ""
//        )
//    }
//
//    private fun createAgent(): Agent {
//        return Agent(
//            id = 1,
//            type = "",
//            name = "",
//            status = "",
//            imageUrl = "",
//            optimisedForMobile = false
//        )
//    }

    @After
    fun tearDown() {
    }
}