package com.skyscanner.repository

import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.entity.response.Response
import com.skyscanner.model.SessionModel
import io.reactivex.Observable

interface FlightRepository {

    fun createSession(query: FlightQuery) : Observable<Response<SessionModel>>
    fun getFlights(sessionurl: String): Observable<Response<FlightResponse>>
}