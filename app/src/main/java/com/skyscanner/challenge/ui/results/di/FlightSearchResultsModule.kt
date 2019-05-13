package com.skyscanner.challenge.ui.results.di

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.skyscanner.challenge.databinding.ActivityFlightResultsBinding
import com.skyscanner.challenge.ui.results.FlightSearchResultsActivity
import com.skyscanner.viewmodel.results.SearchResultsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FlightSearchResultsModule {

    @Provides
    fun inflater(flightSearchResultsActivity: FlightSearchResultsActivity): LayoutInflater {
        return flightSearchResultsActivity.layoutInflater
    }

    @Provides
    fun activityFlightSearchResultsBinding(layoutInflater: LayoutInflater): ActivityFlightResultsBinding {
        return ActivityFlightResultsBinding.inflate(layoutInflater)
    }

    @Singleton
    @Provides
    fun viewModelProvider(
        activity: FlightSearchResultsActivity,
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider {
        return ViewModelProviders.of(activity, viewModelFactory)
    }

    @Provides
    fun viewModel(viewModelProvider: ViewModelProvider): SearchResultsViewModel {
        return viewModelProvider.get(SearchResultsViewModel::class.java)
    }

}