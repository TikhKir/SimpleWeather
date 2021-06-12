package com.example.simpleweather.ui.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.databinding.ItemSavedLocationBinding
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback

class SearchLocationsAdapter(
    private val itemClickListener: OnItemClickListener
):
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val boundedView = ItemSavedLocationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchLocationViewHolder(boundedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val location = getItem(position) as Location
        (holder as SearchLocationViewHolder).bind(location)
    }

    inner class SearchLocationViewHolder(private val binding: ItemSavedLocationBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) {
            binding.textViewLocationBig.text = location.addressCity
            binding.textViewLocationSmall.text = location.addressCounty
            binding.root.setOnClickListener { itemClickListener.onItemClick(location) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(location: Location)
    }
}