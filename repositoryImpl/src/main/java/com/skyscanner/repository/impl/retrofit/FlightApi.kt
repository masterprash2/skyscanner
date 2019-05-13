package com.skyscanner.repository.impl.retrofit

import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.entity.response.SessionResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface FlightApi {


    @POST("pricing/v1.0")
    @Headers("content-type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun createSession(@FieldMap(encoded = false) body: Map<String, String>): Observable<Response<SessionResponse>>

    @GET
    fun getFlights(@Url url: String): Observable<Response<FlightResponse>>
}