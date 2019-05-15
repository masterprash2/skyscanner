package com.skyscanner.interactor.utils

import com.skyscanner.challenge.screen.results.model.item.DirectionModel
import com.skyscanner.interactor.*
import com.skyscanner.interactor.mock.MockPlatformImpl
import com.skyscanner.model.FlightResponseModel
import com.skyscanner.repository.PlatformUtils
import com.squareup.moshi.Moshi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TransformationUtilsTest {

    lateinit var transformationUtils: TransformationUtils
    lateinit var platformUtils: PlatformUtils
    val moshi = Moshi.Builder().build()

    @Before
    fun setUp() {
        platformUtils = MockPlatformImpl()
        transformationUtils = TransformationUtils(platformUtils)
    }


    @Test
    fun checkTransformation() {
        val response = flightResponse(FILE_FLIGHT_RESPONSE_VALID)
        val transformed = transformationUtils.transform(FlightResponseModel.createFrom("sessionUrl", response))
        Assert.assertEquals(3, transformed.size)
        Assert.assertEquals(createItinerary1(), transformed.get(0))
        Assert.assertEquals(createItinerary2(), transformed.get(1))
        Assert.assertEquals(createItinerary3(), transformed.get(2))
    }

    @Test
    fun checkInvalidItemsInresponse() {
        val response = flightResponse(FILE_FLIGHT_RESPONSE_INVALID)
        val transformed = transformationUtils.transform(FlightResponseModel.createFrom("sessionUrl", response))
        Assert.assertEquals(2, transformed.size)
        Assert.assertEquals(createItinerary1(), transformed.get(0))
    }

    @Test
    fun checkInvalidEmptyInbound() {
        val response = flightResponse(FILE_FLIGHT_RESPONSE_INVALID)
        val transformed = transformationUtils.transform(FlightResponseModel.createFrom("sessionUrl", response))
        val itineraryModelWithEmptyInbound = createItinerary3().copy(
            inboundFlight = DirectionModel(
                name = "",
                duration = "",
                stops = "",
                imageUrl = "",
                timeFrame = ""
            )
        )
        Assert.assertEquals(itineraryModelWithEmptyInbound, transformed.get(1))
    }


    @After
    fun tearDown() {
    }

}