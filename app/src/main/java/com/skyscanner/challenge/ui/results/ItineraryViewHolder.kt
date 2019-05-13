package com.skyscanner.challenge.ui.results

import androidx.recyclerview.widget.RecyclerView
import com.skyscanner.challenge.databinding.ItemFlightResultBinding
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel

class ItineraryViewHolder(private val itemBinder: ItemFlightResultBinding) : RecyclerView.ViewHolder(itemBinder.root) {

    fun bind(model: ItineraryModel) {
        itemBinder.model = model;
        itemBinder.inboundImage.setImageUrl(model.inboundFlight.imageUrl)
        itemBinder.outboundImageView.setImageUrl(model.outboundFlight.imageUrl)
//        itemBinder.itineraryRatingImage.setImageUrl(model.ratingImage)
    }

}