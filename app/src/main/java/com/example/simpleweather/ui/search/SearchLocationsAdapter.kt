package com.example.simpleweather.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.item_saved_location.view.*

class SearchLocationsAdapter(
    private val itemClickListener: OnItemClickListener
):
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_location, parent, false)
        return SearchLocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val location = getItem(position) as LocationWithCoords
        (holder as SearchLocationViewHolder).bind(location)
    }

    inner class SearchLocationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(location: LocationWithCoords) {
            itemView.text_view_location_big.text = location.addressCity
            itemView.text_view_location_small.text = location.addressCounty
            itemView.setOnClickListener { itemClickListener.onItemClick(location) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(location: LocationWithCoords)
    }
}