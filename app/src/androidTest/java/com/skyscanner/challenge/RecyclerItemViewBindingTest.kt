package com.skyscanner.challenge

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.activityScenarioRule
import com.skyscanner.challenge.databinding.ItemFlightResultBinding
import com.skyscanner.challenge.screen.results.model.item.DirectionModel
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel
import com.skyscanner.challenge.ui.TestActivity
import com.skyscanner.challenge.ui.results.ItineraryViewHolder
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(androidx.test.runner.AndroidJUnit4::class)
class RecyclerItemViewBindingTest {

    @get:Rule
    var activityScenario = activityScenarioRule<TestActivity>()

    @Test
    fun dataBindingWithViewModel() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        activityScenario.scenario.onActivity {
            val binding = ItemFlightResultBinding.inflate(it.layoutInflater);
            it.setContentView(binding.root)
            validateDataMapping(binding, ItineraryViewHolder(binding), createModelList())
        }
    }

    private fun createModelList(): List<ItineraryModel> {
        return Arrays.asList(createItinerary1(),createItinerary2(),createItinerary3())
    }

    private fun validateDataMapping(
        binding: ItemFlightResultBinding,
        itineraryViewHolder: ItineraryViewHolder,
        list: List<ItineraryModel>
    ) {
        for (itinerary in list) {
            itineraryViewHolder.bind(itinerary)
            binding.executePendingBindings()
            checkMapping(binding, itinerary)
        }
    }

    private fun checkMapping(binding: ItemFlightResultBinding, itineraryModel: ItineraryModel) {
        assertEquals(itineraryModel.agent, binding.agent.text)
        assertEquals(itineraryModel.price, binding.itineraryCost.text)
        assertEquals(itineraryModel.rating, binding.itineraryRating.text)

        assertEquals(itineraryModel.inboundFlight.duration, binding.inboundFlightDuration.text)
        assertEquals(itineraryModel.inboundFlight.stops, binding.inboundFlightStops.text)
        assertEquals(itineraryModel.inboundFlight.name, binding.inboundFlightAgent.text)
        assertEquals(itineraryModel.inboundFlight.timeFrame, binding.inboundTime.text)

        assertEquals(itineraryModel.outboundFlight.duration, binding.outboundFlightDuration.text)
        assertEquals(itineraryModel.outboundFlight.stops, binding.outboundFlightStops.text)
        assertEquals(itineraryModel.outboundFlight.name, binding.outboundFlightAgent.text)
        assertEquals(itineraryModel.outboundFlight.timeFrame, binding.outboundTime.text)

    }


    @After
    fun tearDown() {
        activityScenario.scenario.close()
    }




}