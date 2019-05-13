package com.skyscanner.viewmodel.results

import com.nhaarman.mockitokotlin2.whenever
import com.skyscanner.challenge.entity.network.request.CabinClass
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.challenge.screen.results.model.item.DirectionModel
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel
import com.skyscanner.model.FlightDisplayModel
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.viewmodel.results.data.SearchResultsData
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class SearchResultsPresenterTest {

    lateinit var data: SearchResultsData
    lateinit var presenter: SearchResultsPresenter
    lateinit var platformTools: PlatformUtils

    @Before
    fun setUp() {
        data = SearchResultsData()
        platformTools = Mockito.mock(PlatformUtils::class.java)
        presenter = SearchResultsPresenter(data, platformTools)
    }

    @Test
    fun testFresh() {
        assertFalse(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        assertNull(data.toolbarSubTitle.get())
        assertNull(data.toolbarTitle.get())
        assertFalse(data.areUpdatesPending.get())
        assertNull(data.searchResultsCount.get())
    }

    @Test
    fun testFreshLoading() {

        val dateIn = Calendar.getInstance();
        dateIn.set(Calendar.MONTH, Calendar.NOVEMBER)
        dateIn.set(Calendar.DATE, 16)
        dateIn.set(Calendar.YEAR, 2019)
        val dateOut = Calendar.getInstance();
        dateOut.set(Calendar.YEAR, 2019)
        dateOut.set(Calendar.MONTH, Calendar.NOVEMBER)
        dateOut.set(Calendar.DATE, 12)

        val query = FlightQuery(
            destinationPlace = "des",
            cabinClass = CabinClass.ECONOMY, children = 0, country = "UK",
            currency = "GBP", inboundDate = dateIn.time, outboundDate = dateOut.time,
            infants = 0, locale = "en-GB", locationSchema = "sky", originPlace = "ORI",
            adults = 1
        )

        whenever(platformTools.formatDate("dd MMM", query.inboundDate!!,query.locale)).thenReturn("16 Nov")
        whenever(platformTools.formatDate("dd MMM", query.outboundDate,query.locale)).thenReturn("12 Nov")
        presenter.handleLoadEvent(query);
        assertFalse(data.areUpdatesPending.get())
        assertFalse(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertTrue(data.isLoading.get())
        assertNotNull(data.toolbarSubTitle.get())
        assertNotNull(data.toolbarTitle.get())
        assertEquals("ORI - des", data.toolbarTitle.get())
        assertEquals("12 Nov - 16 Nov, 1 adult, economy", data.toolbarSubTitle.get())
        assertEquals("Loading",data.searchResultsCount.get())
    }

    @Test
    fun testFreshLoadFailed() {
        presenter.handleLoadFailed();
        assertFalse(data.areUpdatesPending.get())
        assertFalse(data.isContentAvailable.get())
        assertTrue(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        assertEquals("Failed",data.searchResultsCount.get())
    }

    @Test
    fun loadSuccess() {
        val itineraryModel = Arrays.asList(createItinerary("agentName"))
        val flightDisplayModel = FlightDisplayModel(itineraryModel, true)
        presenter.handleLoadSuccess(flightDisplayModel)
        assertTrue(data.areUpdatesPending.get())
        assertTrue(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        val observer = TestObserver.create<List<ItineraryModel>>()
        data.observeFlightsData().subscribe(observer)
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValueAt(0,itineraryModel)
        assertEquals("1 Polling",data.searchResultsCount.get())
    }

    @Test
    fun multipleFlightResponseSuccess() {
        val itineraryModel = Arrays.asList(createItinerary("first"))
        val flightDisplayModel = FlightDisplayModel(itineraryModel, true)
        presenter.handleLoadSuccess(flightDisplayModel)
        assertTrue(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        assertTrue(data.areUpdatesPending.get())
        val observer = TestObserver.create<List<ItineraryModel>>()
        data.observeFlightsData().subscribe(observer)
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValueAt(0,itineraryModel)
        assertEquals("1 Polling",data.searchResultsCount.get())

        val secondResponse = Arrays.asList(createItinerary("second"),createItinerary("third"),createItinerary("fourth"));
        presenter.handleLoadSuccess(FlightDisplayModel(secondResponse, false))
        observer.assertNoErrors()
        observer.assertValueCount(2)
        observer.assertValueAt(1,secondResponse)
        assertFalse(data.areUpdatesPending.get())
        assertEquals("3",data.searchResultsCount.get())
    }

    @Test
    fun firstFailedNextSuccess() {
        presenter.handleLoadFailed()
        assertFalse(data.isContentAvailable.get())
        assertTrue(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        val createItinerary = createItinerary("first")
        presenter.handleLoadSuccess(FlightDisplayModel(Arrays.asList(createItinerary),false))
        assertTrue(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        val observer = TestObserver<List<ItineraryModel>>()
        data.observeFlightsData().subscribe(observer)
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValueAt(0,Arrays.asList(createItinerary))
        assertEquals("1",data.searchResultsCount.get())
    }


    @Test
    fun firstSuccessWithUpdatesPendingNextFailed() {
        val createItinerary = createItinerary("first")
        presenter.handleLoadSuccess(FlightDisplayModel(Arrays.asList(createItinerary),true))
        assertTrue(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        val observer = TestObserver<List<ItineraryModel>>()
        data.observeFlightsData().subscribe(observer)
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValueAt(0,Arrays.asList(createItinerary))
        assertEquals("1 Polling",data.searchResultsCount.get())

        presenter.handleLoadFailed()
        assertTrue(data.isContentAvailable.get())
        assertFalse(data.isErrorLoading.get())
        assertFalse(data.isLoading.get())
        assertFalse(data.areUpdatesPending.get())
        assertEquals("1",data.searchResultsCount.get())
    }


    private fun createItinerary(agentName: String): ItineraryModel {
        return ItineraryModel(
            inboundFlight = DirectionModel(
                name = "Wiz",
                stops = "2 Stops",
                duration = "1h 25m",
                imageUrl = "",
                timeFrame = "16:00 - 19:45"
            ),
            price = "123",
            agent = agentName,
            outboundFlight = DirectionModel(
                name = "Wiz",
                stops = "Direct",
                duration = "5h 35m",
                imageUrl = "",
                timeFrame = "01:00 - 15:45"
            ),
            rating = "9",
            ratingImage = ""
        )
    }


    @After
    fun tearDown() {
    }
}