package com.skyscanner.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.skyscanner.entity.response.Response
import com.skyscanner.entity.response.SessionResponse
import com.skyscanner.entity.response.ValidationError
import com.skyscanner.model.SessionModel
import com.skyscanner.repository.FlightRepository
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.lang.NullPointerException
import java.util.*

class CreateSessionInteractorTest {

    lateinit var createSessionInteractor: CreateSessionInteractor
    lateinit var flightRepository: FlightRepository

    @Before
    fun setUp() {
        flightRepository = Mockito.mock(FlightRepository::class.java)
        createSessionInteractor = CreateSessionInteractor(flightRepository)
    }

    @Test
    fun testCreateSessionSuccess() {
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(successSessionResponse()))
        val observer = TestObserver<Response<SessionModel>>()
        createSessionInteractor.createSession(TestDataHelper.dummyFlightQuery()).subscribe(observer)
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        assertTrue(response.success)
        assertEquals("sessionUrl", response.value?.sessionUrl)
    }

    @Test
    fun testCreateSessionFailed() {
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(failedAuthSessionResponse()))
        val observer = TestObserver<Response<SessionModel>>()
        createSessionInteractor.createSession(TestDataHelper.dummyFlightQuery()).subscribe(observer)
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        assertFalse(response.success)
        assertNull(response.value?.sessionUrl)
    }


    @Test
    fun testForNoObservableError() {
        whenever(flightRepository.createSession(any())).thenReturn(Observable.error(NullPointerException()))
        val observer = TestObserver<Response<SessionModel>>()
        createSessionInteractor.createSession(TestDataHelper.dummyFlightQuery()).subscribe(observer)
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        assertFalse(response.success)
        assertNull(response.value?.sessionUrl)
    }

    @Test
    fun testCreateSession200SuccessNullUrl() {
        whenever(flightRepository.createSession(any())).thenReturn(Observable.just(succes200NullSessionUrl()))
        val observer = TestObserver<Response<SessionModel>>()
        createSessionInteractor.createSession(TestDataHelper.dummyFlightQuery()).subscribe(observer)
        assertTrue(observer.values().isNotEmpty())
        assertEquals(1, observer.valueCount())
        val response = observer.values().first()
        assertFalse(response.success)
        assertNull(response.value?.sessionUrl)
        assertNotNull(response.exception)
    }


    companion object {
        fun successSessionResponse() : Response<SessionModel> {
            return Response(
                value = SessionModel("sessionUrl", SessionResponse(null, null)),
                success = true
            )
        }

        fun failedAuthSessionResponse() : Response<SessionModel> {
            val asList = Arrays.asList(ValidationError(message = "error message", parameterName = "keyname"))
            return Response(
                value = SessionModel(null, SessionResponse(asList, null)),
                success = false
            )
        }

        fun succes200NullSessionUrl() : Response<SessionModel> {
            val asList = Arrays.asList(ValidationError(message = "error message", parameterName = "keyname"))
            return Response(
                value = SessionModel(null, SessionResponse(asList, null)),
                success = true
            )
        }
    }

    @After
    fun tearDown() {
    }
}