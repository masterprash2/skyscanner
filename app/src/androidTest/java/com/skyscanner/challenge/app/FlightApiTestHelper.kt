package com.skyscanner.challenge.app

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.challenge.flightResponseFromAssets
import com.skyscanner.entity.response.SessionResponse
import com.skyscanner.repository.impl.retrofit.FlightApi
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*

class FlightApiTestHelper(private val context: Context) : FlightApi {

    var sessionResponse: PublishSubject<Response<SessionResponse>> = PublishSubject.create()
    var flightResponse: PublishSubject<Response<FlightResponse>> = PublishSubject.create()


    override fun createSession(body: Map<String, String>): Observable<Response<SessionResponse>> {
        return sessionResponse
    }

    override fun getFlights(url: String): Observable<Response<FlightResponse>> {
        return flightResponse
    }

    fun sendSessionResponseFailed() {
        sessionResponse.onNext(
            Response.error(
                403,
                ResponseBody.Companion.create(contentType = MediaType.get("text/html"), content = "")
            )
        )
    }

    fun setFlightResponseFailed() {
        flightResponse.onNext(
            Response.error(
                403,
                ResponseBody.Companion.create(contentType = MediaType.get("text/html"), content = "")
            )
        )
    }

    fun sendSessionResponseSuccess(sessionUrl: String) {
        sessionResponse.onNext(
            Response.success(
                SessionResponse(null, null),
                Headers.of(Collections.singletonMap("location", sessionUrl))
            )
        )
    }

    fun sendFlightResponseWithName(response: FlightResponse) {
        flightResponse.onNext(Response.success(response))
    }

    fun readResponseFrom(fileName: String): FlightResponse {
        val context = InstrumentationRegistry.getInstrumentation().context
        return flightResponseFromAssets(context, fileName)
    }

}