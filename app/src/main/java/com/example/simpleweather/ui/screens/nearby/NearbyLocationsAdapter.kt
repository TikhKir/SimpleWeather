package com.example.simpleweather.ui.screens.nearby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.databinding.ItemSavedLocationBinding
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback

class NearbyLocationsAdapter(
    private val itemClickListener: OnItemClickListener
) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val boundedView = ItemSavedLocationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NearbyLocationViewHolder(boundedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val location = getItem(position) as LocationWithCoords
        (holder as NearbyLocationViewHolder).bind(location)
    }

    inner class NearbyLocationViewHolder(private val binding: ItemSavedLocationBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationWithCoords) {
            binding.textViewLocationBig.text = location.addressCity
            binding.textViewLocationSmall.text = location.addressCounty
            binding.root.setOnClickListener { itemClickListener.onItemClick(location) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(location: LocationWithCoords)
    }
}