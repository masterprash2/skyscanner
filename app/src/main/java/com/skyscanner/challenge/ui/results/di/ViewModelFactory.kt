package com.skyscanner.challenge.ui.results.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skyscanner.viewmodel.results.SearchResultsViewModel
import com.skyscanner.viewmodel.results.SearchResultsViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(private val provider: Provider<SearchResultsViewModelFactory>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return provider.get().create() as T
    }


}