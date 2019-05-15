package com.skyscanner.challenge.ui.results

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.skyscanner.challenge.FILE_FLIGHT_RESPONSE_INVALID
import com.skyscanner.challenge.FILE_FLIGHT_RESPONSE_VALID
import com.skyscanner.challenge.R
import com.skyscanner.challenge.app.FlightApiTestHelper
import com.skyscanner.challenge.app.SkyscannerTestApplication
import com.skyscanner.challenge.app.di.SkyscannerTestAppComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlightSearchResultsActivityTest {

    @get:Rule
    var activityScenario = activityScenarioRule<FlightSearchResultsActivity>()

    lateinit var flightApiTestHelper: FlightApiTestHelper

    @Before
    fun setUp() {
        val skyscannerTestApplication =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as SkyscannerTestApplication
        val skyscannerTestAppComponent = skyscannerTestApplication.androidInjector as SkyscannerTestAppComponent
        flightApiTestHelper = skyscannerTestAppComponent.flightApiHelper()
    }

    @Test
    fun successLoad() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.scenario.onActivity {
            it.viewBinder.executePendingBindings()
        }
        onIdle()
        onView(withId(R.id.progressBar)).check(ViewAssertions.matches(isDisplayed()))
        flightApiTestHelper.sendSessionResponseSuccess("sessionUrl")

        val readResponseFrom = flightApiTestHelper.readResponseFrom(FILE_FLIGHT_RESPONSE_VALID)
        flightApiTestHelper.sendFlightResponseWithName(readResponseFrom)
        onIdle()
        onView(withId(R.id.searchResults)).check(RecyclerViewItemCountAssertion(3))
    }


    @Test
    fun loadingFailed() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.scenario.onActivity {
            it.viewBinder.executePendingBindings()
        }
        onIdle()
        onView(withId(R.id.progressBar)).check(ViewAssertions.matches(isDisplayed()))
        flightApiTestHelper.sendSessionResponseSuccess("sessionUrl")

        flightApiTestHelper.setFlightResponseFailed()
        onIdle()
        activityScenario.scenario.onActivity { it.viewBinder.executePendingBindings() }
        onIdle()
        onView(withId(R.id.retryButton)).check(matches(isDisplayed()))
    }

    @Test
    fun loadingFailedRetrySuccess() {
        loadingFailed()
        onView(withId(R.id.retryButton)).perform(click())
        onIdle()
        activityScenario.scenario.onActivity { it.viewBinder.executePendingBindings() }
        onIdle()
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        flightApiTestHelper.sendFlightResponseWithName(flightApiTestHelper.readResponseFrom(FILE_FLIGHT_RESPONSE_VALID))
        onIdle()
        activityScenario.scenario.onActivity { it.viewBinder.executePendingBindings() }
        onIdle()
        onView(withId(R.id.searchResults)).check(RecyclerViewItemCountAssertion(3))
    }

    @After
    fun tearDown() {
    }
}