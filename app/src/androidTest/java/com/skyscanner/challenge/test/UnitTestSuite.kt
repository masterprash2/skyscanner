package com.skyscanner.challenge.test

import com.skyscanner.challenge.ActivityViewBindingTest
import com.skyscanner.challenge.RecyclerItemViewBindingTest
import com.skyscanner.challenge.ui.results.FlightSearchResultsActivity
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests.
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ActivityViewBindingTest::class,
    RecyclerItemViewBindingTest::class,
    FlightSearchResultsActivity::class
)
class UnitTestSuite