package com.example.simpleweather.ui.conditiondetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.item_hourly_condition.view.*

class HourlyConditionalAdapter(private val itemWidth: Int) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_condition, parent, false)
        val tempParam = view.layoutParams
        tempParam.width = itemWidth
        view.layoutParams = tempParam
        return HourlyConditionalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hourlyCondition = getItem(position) as HourlyWeatherCondition
        (holder as HourlyConditionalViewHolder).bind(hourlyCondition)
    }

    inner class HourlyConditionalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(hourlyCondition: HourlyWeatherCondition) {
            itemView.text_view_condition_item_time.text = hourlyCondition.timeStamp.toString()
            itemView.text_view_condition_item_wind.text = hourlyCondition.windSpeed.toString()
            itemView.image_view_condition_item.setImageResource(R.drawable.ic_condition_02d)
        }
    }

}