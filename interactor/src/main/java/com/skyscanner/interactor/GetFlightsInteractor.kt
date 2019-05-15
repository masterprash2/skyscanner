package com.skyscanner.interactor

import com.skyscanner.challenge.entity.network.search.FlightResponse
import com.skyscanner.entity.response.Response
import com.skyscanner.interactor.utils.TransformationUtils
import com.skyscanner.model.FlightDisplayModel
import com.skyscanner.model.FlightResponseModel
import com.skyscanner.repository.FlightRepository
import com.skyscanner.repository.PlatformUtils
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetFlightsInteractor @Inject constructor(
    private val flightRepository: FlightRepository,
    private val transformationUtils: TransformationUtils,
    private val platformUtils: PlatformUtils
) {

    fun getFlights(sessionurl: String): Observable<Response<FlightDisplayModel>> {
        return pollFlights(sessionurl)
            .onErrorReturn {
                Response<FlightResponse>(success = false, value = null).apply {
                    exception = it
                }
            }.map { transform(sessionurl, it) }.onErrorReturn {
                Response<FlightDisplayModel>(null, false).apply { exception = it }
            }
    }

    private fun pollFlights(sessionurl: String): Observable<Response<FlightResponse>> {
        return flightRepository.getFlights(sessionurl).repeatWhen {
            platformUtils.delay(it,1, TimeUnit.SECONDS)
        }.takeUntil {
            shouldStopPolling(it)
        }
    }

    private fun shouldStopPolling(it: Response<FlightResponse>): Boolean {
        val requestFailed = !it.success
        val isResponseNull = it.value == null
        return requestFailed || isResponseNull || it.value!!.status.equals("UpdatesComplete", true)
    }

    private fun transform(sessionurl: String, response: Response<FlightResponse>): Response<FlightDisplayModel> {
        val newResponse: Response<FlightDisplayModel>
        if (response.success) {
            if (response.value != null) {
                val model =
                    FlightResponseModel.createFrom(sessionUrl = sessionurl, flightResponse = response.value!!)
                val updatesPending = !response.value!!.status.equals("UpdatesComplete", true)
                val flightDisplayModel = FlightDisplayModel(
                    itineraries = transformationUtils.transform(model),
                    areUpdatesPending = updatesPending
                )
                newResponse = Response(flightDisplayModel, true)
            } else {
                newResponse = Response<FlightDisplayModel>(null, false).apply {
                    exception = NullPointerException("Null data")
                }
            }
        } else {
            newResponse = Response<FlightDisplayModel>(null, false).apply { exception = response.exception }
        }
        return newResponse
    }


}