package com.skyscanner.challenge

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.activityScenarioRule
import com.nhaarman.mockitokotlin2.verify
import com.skyscanner.challenge.ui.TestActivity
import com.skyscanner.challenge.ui.results.SearchResultsAdapter
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.*

@RunWith(androidx.test.runner.AndroidJUnit4::class)
class SearchResultsAdapterTest {

    @get:Rule
    var activityScenario = activityScenarioRule<TestActivity>()

    @Test
    fun adpaterContentSetTest() {
        activityScenario.scenario.onActivity {
            val adapter = SearchResultsAdapter(it.layoutInflater)
            val dataObserver = Mockito.mock(RecyclerView.AdapterDataObserver::class.java)
            adapter.registerAdapterDataObserver(dataObserver)
            assertEquals(0,adapter.itemCount)

            adapter.updateWithNewList(Arrays.asList(createItinerary1(), createItinerary2(), createItinerary3()))
            verify(dataObserver).onItemRangeInserted(0,3)
            assertEquals(3,adapter.itemCount)

            adapter.updateWithNewList(Arrays.asList(createItinerary3()))
            verify(dataObserver).onItemRangeRemoved(0,3)
            verify(dataObserver).onItemRangeInserted(0,1)
            assertEquals(1,adapter.itemCount)
        }
    }

}