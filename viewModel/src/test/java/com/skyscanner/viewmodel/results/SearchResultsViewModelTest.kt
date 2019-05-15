package com.skyscanner.viewmodel.results

import com.nhaarman.mockitokotlin2.*
import com.skyscanner.challenge.entity.network.request.CabinClass
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.challenge.entity.network.search.*
import com.skyscanner.entity.response.Response
import com.skyscanner.entity.response.SessionResponse
import com.skyscanner.interactor.CreateSessionInteractor
import com.skyscanner.interactor.GetFlightsInteractor
import com.skyscanner.interactor.utils.TransformationUtils
import com.skyscanner.model.SessionModel
import com.skyscanner.repository.FlightRepository
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.viewmodel.results.TestDataHelper.Companion.dummyFlightResponse
import com.skyscanner.viewmodel.results.data.SearchResultsData
import com.skyscanner.viewmodel.results.mock.MockPlatformImpl
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*
import java.util.concurrent.TimeUnit

class SearchResultsViewModelTest {

    lateinit var viewModel: SearchResultsViewModel
    lateinit var presenter: SearchResultsPresenter
    lateinit var data: SearchResultsData
    lateinit var platformUtils: PlatformUtils
    lateinit var createSessionInteractor: CreateSessionInteractor
    lateinit var getFlightsInteractor: GetFlightsInteractor
    lateinit var flightRepository: FlightRepository
    lateinit var transformationUtils: TransformationUtils

    @Before
    fun setUp() {
        flightRepository = Mockito.mock(FlightRepository::class.java)
        platformUtils = MockPlatformImpl()
        transformationUtils = TransformationUtils(platformUtils)
        getFlightsInteractor = GetFlightsInteractor(flightRepository, transformationUtils,platformUtils)
        createSessionInteractor = CreateSessionInteractor(flightRepository)
        data = SearchResultsData()
        presenter = SearchResultsPresenter(data, platformUtils)
        viewModel = SearchResultsViewModel(presenter, createSessionInteractor, getFlightsInteractor)
    }

    @Test
    fun searchFlightSuccess() {
        var query = dummyQuery();
        query = query.copy(adults = 2)

        val sessionResponse = Response(SessionModel("sessionUrl", SessionResponse(null, null)), true)
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(sessionResponse))


        val dataResponse = Response(dummyFlightResponse(),true)
        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.just(dataResponse))

        viewModel.searchFlight(query)
        Assert.assertTrue(data.isContentAvailable.get())
        Assert.assertFalse(data.isErrorLoading.get())
        Assert.assertFalse(data.isLoading.get())
        Assert.assertNotNull(data.toolbarSubTitle.get())
        Assert.assertNotNull(data.toolbarTitle.get())
        Assert.assertEquals("ORI - des", data.toolbarTitle.get())
        Assert.assertEquals("13 Nov - 15 Nov, 2 adults, economy", data.toolbarSubTitle.get())
    }


    @Test
    fun searchFlightSessionFailed() {
        val query = dummyQuery();

        val sessionResponse = Response(SessionModel("sessionUrl", SessionResponse(null, null)), false)
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(sessionResponse))


        val dataResponse = Response<FlightResponse>(dummyFlightResponse(),true)
        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.just(dataResponse))

        viewModel.searchFlight(query)
        Assert.assertFalse(data.isContentAvailable.get())
        Assert.assertTrue(data.isErrorLoading.get())
        Assert.assertFalse(data.isLoading.get())
        Assert.assertNotNull(data.toolbarSubTitle.get())
        Assert.assertNotNull(data.toolbarTitle.get())
        Assert.assertEquals("ORI - des", data.toolbarTitle.get())
        Assert.assertEquals("13 Nov - 15 Nov, 1 adult, economy", data.toolbarSubTitle.get())
    }

    @Test
    fun searchFlightResultsFailed() {val query = dummyQuery();

        val sessionResponse = Response(SessionModel("sessionUrl", SessionResponse(null, null)), true)
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(sessionResponse))


        val dataResponse = Response<FlightResponse>(TestDataHelper.dummyFlightResponse(),false)
        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.just(dataResponse))

        viewModel.searchFlight(query)
        Assert.assertEquals("sessionUrl",data.sessionUrl)
        Assert.assertFalse(data.isContentAvailable.get())
        Assert.assertTrue(data.isErrorLoading.get())
        Assert.assertFalse(data.isLoading.get())
        Assert.assertNotNull(data.toolbarSubTitle.get())
        Assert.assertNotNull(data.toolbarTitle.get())
        Assert.assertEquals("ORI - des", data.toolbarTitle.get())
        Assert.assertEquals("13 Nov - 15 Nov, 1 adult, economy", data.toolbarSubTitle.get())

    }


    @Test
    fun searchFlightFirstFailedThenSuccess() {val query = dummyQuery();
        searchFlightResultsFailed()
        searchFlightSuccess()
    }


    @Test
    fun checkParallelRequestsFresh() {
        whenever(flightRepository.createSession(any())).thenReturn(Observable.never())
        viewModel.searchFlight(dummyQuery())
        viewModel.searchFlight(dummyQuery())
        verify(flightRepository, times(1)).createSession(any())
    }


    @Test
    fun checkParallelRequestsForResultsFailure() {
        val dummyQuery = dummyQuery();
        val sessionResponse = Response(SessionModel("sessionUrl", SessionResponse(null, null)), true)
        whenever(flightRepository.createSession(dummyQuery)).thenReturn(Observable.just(sessionResponse))
        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.just(Response<FlightResponse>(null,false)))
        viewModel.searchFlight(dummyQuery)
        viewModel.searchFlight(dummyQuery)
        verify(flightRepository, times(1)).createSession(any())
        verify(flightRepository, times(2)).getFlights(any())


    }

    @Test
    fun checkParallelExecutionFirstFailureThenWaiting() {

        val dummyQuery = dummyQuery();
        val sessionResponse = Response(SessionModel("sessionUrl", SessionResponse(null, null)), true)
        whenever(flightRepository.createSession(dummyQuery)).thenReturn(Observable.just(sessionResponse))
        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.just(Response<FlightResponse>(null,false)))
        viewModel.searchFlight(dummyQuery)
        viewModel.searchFlight(dummyQuery)

        whenever(flightRepository.getFlights("sessionUrl")).thenReturn(Observable.never())
        viewModel.searchFlight(dummyQuery)
        viewModel.searchFlight(dummyQuery)
        viewModel.searchFlight(dummyQuery)
        viewModel.searchFlight(dummyQuery)
        viewModel.onRetryClicked()
        verify(flightRepository,times(1)).createSession(any())
        verify(flightRepository, times(3)).getFlights(any())
    }







    fun dummyQuery(): FlightQuery {
        val dateIn = Calendar.getInstance();
        dateIn.timeZone = TimeZone.getTimeZone("GMT")
        dateIn.set(Calendar.MONTH, Calendar.NOVEMBER)
        dateIn.set(Calendar.DATE, 15)
        dateIn.set(Calendar.YEAR, 2019)
        dateIn.set(Calendar.HOUR_OF_DAY,12)
        val dateOut = Calendar.getInstance();
        dateOut.timeZone = TimeZone.getTimeZone("GMT")
        dateOut.set(Calendar.YEAR, 2019)
        dateOut.set(Calendar.MONTH, Calendar.NOVEMBER)
        dateOut.set(Calendar.DATE, 13)
        dateOut.set(Calendar.HOUR_OF_DAY,12)

        return FlightQuery(
            destinationPlace = "des",
            cabinClass = CabinClass.ECONOMY, children = 0, country = "UK",
            currency = "GBP", inboundDate = dateIn.time, outboundDate = dateOut.time,
            infants = 0, locale = "en-GB", locationSchema = "sky", originPlace = "ORI",
            adults = 1
        )
    }




    @After
    fun tearDown() {
    }
}