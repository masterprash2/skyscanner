package com.skyscanner.challenge

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skyscanner.challenge.databinding.ActivityFlightResultsBinding
import com.skyscanner.viewmodel.results.data.SearchResultsData
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityViewBindingTest {

    @get:Rule
    var activityScenario = activityScenarioRule<BlankActivity>()

    @Test
    fun dataBindingWithViewModel() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.scenario.onActivity {
            val binding = ActivityFlightResultsBinding.inflate(it.layoutInflater);
            it.setContentView(binding.root)
            validateDataMapping(binding)
        }
    }

    private fun validateDataMapping(binding: ActivityFlightResultsBinding) {
        val data = SearchResultsData()
        binding.data = data;
        checkSearchResulstCount(data,binding)
        checkProgressBarBinding(data,binding)
        checkToolbarText(data,binding)
        checkToolbarSubTitle(data,binding)
        checkRetryView(data,binding)
    }

    private fun checkRetryView(data: SearchResultsData, binding: ActivityFlightResultsBinding) {
        data.isErrorLoading.set(false)
        binding.executePendingBindings()
        assertNotEquals(View.VISIBLE,binding.retryButton.visibility)

        data.isErrorLoading.set(true)
        binding.executePendingBindings()
        assertEquals(View.VISIBLE,binding.retryButton.visibility)
    }

    private fun checkToolbarText(data: SearchResultsData, binding: ActivityFlightResultsBinding) {
        data.toolbarTitle.set(null)
        binding.executePendingBindings()
        assertNull(binding.toolbar.title)

        data.toolbarTitle.set("TestContent")
        binding.executePendingBindings()
        assertEquals("TestContent",binding.toolbar.title)
    }

    private fun checkToolbarSubTitle(data: SearchResultsData, binding: ActivityFlightResultsBinding) {
        data.toolbarSubTitle.set(null)
        binding.executePendingBindings()
        assertNull(binding.toolbar.subtitle)

        data.toolbarSubTitle.set("TestContent")
        binding.executePendingBindings()
        assertEquals("TestContent",binding.toolbar.subtitle)
    }

    private fun checkProgressBarBinding(data: SearchResultsData, binding: ActivityFlightResultsBinding) {
        data.isLoading.set(false)
        binding.executePendingBindings()
        assertNotEquals(View.VISIBLE,binding.progressBar.visibility)

        data.isLoading.set(true)
        binding.executePendingBindings()
        assertEquals(View.VISIBLE,binding.progressBar.visibility)

    }

    private fun checkSearchResulstCount(
        data: SearchResultsData,
        binding: ActivityFlightResultsBinding
    ) {
        data.searchResultsCount.set("0")
        binding.executePendingBindings()
        assertEquals("0", binding.resultsCount.text)

        data.searchResultsCount.set("anyText")
        binding.executePendingBindings()
        assertEquals("anyText", binding.resultsCount.text)

        data.searchResultsCount.set("1 Polling")
        binding.executePendingBindings()
        assertEquals("1 Polling", binding.resultsCount.text)
    }

    @After
    fun tearDown() {
    }


}