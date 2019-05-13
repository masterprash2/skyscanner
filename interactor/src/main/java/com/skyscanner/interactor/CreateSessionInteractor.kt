package com.skyscanner.interactor

import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.entity.response.Response
import com.skyscanner.model.SessionModel
import com.skyscanner.repository.FlightRepository
import io.reactivex.Observable
import javax.inject.Inject

class CreateSessionInteractor @Inject constructor(private val flightRepository: FlightRepository) {
    fun createSession(query: FlightQuery): Observable<Response<SessionModel>> {
        return flightRepository.createSession(query)
            .onErrorReturn {
                Response<SessionModel>(success = false, value = null).apply {
                    exception = it
                }
            }.map { validateResponse(it) }
    }

    private fun validateResponse(response: Response<SessionModel>): Response<SessionModel>? {
        if (response.success && (response.value == null || response.value!!.sessionUrl.isNullOrBlank())) {
            return Response(response.value, false).apply { exception = NullPointerException("Session Url is Null") }
        } else {
            return response;
        }
    }
}