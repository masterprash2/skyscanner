package com.skyscanner.viewmodel.results.data

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class SearchResultsData @Inject constructor() {

    var sessionUrl: String? = null
    var query: FlightQuery? = null

    private val flightsDataPublisher = BehaviorSubject.create<List<ItineraryModel>>()
    val isLoading: ObservableBoolean = ObservableBoolean(false)
    val isErrorLoading = ObservableBoolean(false)
    val isContentAvailable = ObservableBoolean(false)
    val areUpdatesPending = ObservableBoolean(false)
    var contentSize: Int = 0

    val toolbarTitle = ObservableField<String>()
    val toolbarSubTitle = ObservableField<String>()

    val searchResultsCount = ObservableField<String>()

    fun observeFlightsData(): Observable<List<ItineraryModel>> {
        return flightsDataPublisher;
    }

    fun updateFlightsData(data: List<ItineraryModel>) {
        flightsDataPublisher.onNext(data)
    }


}