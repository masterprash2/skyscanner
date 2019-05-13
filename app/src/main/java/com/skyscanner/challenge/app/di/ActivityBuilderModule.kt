package com.skyscanner.challenge.app.di

import com.skyscanner.challenge.ui.results.di.FlightSearchResultsModule
import com.skyscanner.challenge.ui.results.FlightSearchResultsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class ActivityBuilderModule {

    @Singleton
    @ContributesAndroidInjector(modules = [FlightSearchResultsModule::class])
    internal abstract fun flightSearchResultsActivity(): FlightSearchResultsActivity


}