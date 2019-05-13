package com.skyscanner.viewmodel.results

import com.skyscanner.entity.request.FlightQuery
import com.skyscanner.model.FlightDisplayModel
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.viewmodel.results.data.SearchResultsData
import javax.inject.Inject

class SearchResultsPresenter @Inject constructor(
    val data: SearchResultsData,
    private val platformTools: PlatformUtils
) {

    init {
        data.isErrorLoading.set(false)
        data.isLoading.set(false)
        data.isContentAvailable.set(false)
    }

    fun handleLoadEvent(query: FlightQuery) {
        data.isLoading.set(true)
        data.isErrorLoading.set(false)
        data.toolbarTitle.set(query.originPlace + " - " + query.destinationPlace)
        data.toolbarSubTitle.set(displayQueryString(query))
        if(!data.isContentAvailable.get()) {
           data.searchResultsCount.set("Loading")
        }
    }


    private fun displayQueryString(flightQuery: FlightQuery): String {
        val sb = StringBuilder()
        sb.append(platformTools.formatDate("dd MMM", flightQuery.outboundDate,flightQuery.locale))
        if (flightQuery.inboundDate != null) {
            sb.append(" - ").append(platformTools.formatDate("dd MMM", flightQuery.inboundDate!!,flightQuery.locale))
        }
        sb.append(", ").append(flightQuery.adults).append(" adult")
        if (flightQuery.adults > 1) {
            sb.append("s")
        }
//        if(children == 1)
//            sb.append(children)
//        else if(children > 1) {
//            sb.append(children).append()
//        }
        if (flightQuery.cabinClass != null)
            sb.append(", ").append(flightQuery.cabinClass!!.cabinClassName)
        return sb.toString()
    }

    fun handleLoadFailed() {
        data.isLoading.set(false)
        data.areUpdatesPending.set(false)
        if (data.isContentAvailable.get()) {
            data.isErrorLoading.set(false)
        } else {
            data.isErrorLoading.set(true)
        }
        if(data.isContentAvailable.get())
            data.searchResultsCount.set(data.contentSize.toString())
        else {
            data.searchResultsCount.set("Failed")
        }
    }

    fun handleLoadSuccess(responseModel: FlightDisplayModel) {
        val itineraries = responseModel.itineraries
        data.areUpdatesPending.set(responseModel.areUpdatesPending)
        data.isErrorLoading.set(false)
        data.isLoading.set(false)
        data.isContentAvailable.set(itineraries.isNotEmpty())
        data.updateFlightsData(itineraries)
        data.searchResultsCount.set(resultsCountText(responseModel))
        data.contentSize = responseModel.itineraries.size
    }

    fun updateSessionUrl(sessionUrl: String?) {
        data.sessionUrl = sessionUrl;
    }

    private fun resultsCountText(responseModel: FlightDisplayModel): String {
        return if(responseModel.areUpdatesPending)
            responseModel.itineraries.size.toString() + " Polling"
        else
            responseModel.itineraries.size.toString()
    }


}