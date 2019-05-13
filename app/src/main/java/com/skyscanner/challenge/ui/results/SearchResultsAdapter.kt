package com.skyscanner.challenge.ui.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skyscanner.challenge.databinding.ItemFlightResultBinding
import com.skyscanner.challenge.screen.results.model.item.ItineraryModel

class SearchResultsAdapter (
    private val inflater: LayoutInflater
) : RecyclerView.Adapter<ItineraryViewHolder>() {

    private var resultsList: List<ItineraryModel> = ArrayList()

    fun updateWithNewList(newList: List<ItineraryModel>) {
        notifyItemRangeRemoved(0, resultsList.size)
        resultsList = newList
        notifyItemRangeInserted(0, newList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        return ItineraryViewHolder(ItemFlightResultBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.bind(resultsList.get(position))
    }

}