package com.skyscanner.viewmodel.results

import androidx.lifecycle.ViewModel
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.entity.response.Response
import com.skyscanner.interactor.CreateSessionInteractor
import com.skyscanner.interactor.GetFlightsInteractor
import com.skyscanner.model.FlightDisplayModel
import com.skyscanner.model.SessionModel
import com.skyscanner.viewmodel.results.data.SearchResultsData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AutoFactory
open class SearchResultsViewModel @Inject constructor(
    @Provided private val presenter: SearchResultsPresenter,
    @Provided private val createSession: CreateSessionInteractor,
    @Provided private val getFlightsInteractor: GetFlightsInteractor
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun resultsData(): SearchResultsData {
        return presenter.data;
    }

    fun onRetryClicked() {
        searchFlight(resultsData().query!!)
    }

    fun searchFlight(query: FlightQuery) {
        if (!resultsData().isLoading.get()) {
            resultsData().query = query
            presenter.handleLoadEvent(query)
            checkSessionAndProceed(query)
        }
    }

    private fun checkSessionAndProceed(query: FlightQuery) {
        if (resultsData().sessionUrl.isNullOrBlank()) {
            compositeDisposable.add(createSession.createSession(query).subscribe {
                handleSessionResponse(it)
            })
        } else {
            searchFlights(resultsData().sessionUrl!!)
        }
    }

    private fun handleSessionResponse(response: Response<SessionModel>) {
        if (response.success) {
            val sessionUrl = response.value!!.sessionUrl!!
            presenter.updateSessionUrl(sessionUrl)
            searchFlights(sessionUrl)
        } else {
            presenter.handleLoadFailed()
        }
    }

    private fun searchFlights(sessionUrl: String) {
        compositeDisposable.add(getFlightsInteractor.getFlights(sessionUrl).subscribe {
            handleFlightResponse(it)
        })
    }

    private fun handleFlightResponse(response: Response<FlightDisplayModel>) {
        if (response.success) {
            presenter.handleLoadSuccess(response.value!!)
        } else {
            presenter.handleLoadFailed()
        }
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}