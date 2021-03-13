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
import com.example.simpleweather.utils.iconconverter.IconConverter
import kotlinx.android.synthetic.main.item_hourly_condition.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class HourlyConditionalAdapter(private val itemWidth: Int) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_condition, parent, false)
        val tempParam = view.layoutParams
        tempParam.width = itemWidth
        view.layoutParams = tempParam
        return HourlyConditionalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hourlyCondition = getItem(position) as HourlyWeatherCondition
        (holder as HourlyConditionalViewHolder).bind(hourlyCondition)
    }

    inner class HourlyConditionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hourlyCondition: HourlyWeatherCondition) {
            val iconId = hourlyCondition.weatherId?.let { IconConverter.idToIcon(it, false) }
            val allVolume = (hourlyCondition.rainVolume ?: 0F) + (hourlyCondition.snowVolume ?: 0F)
            val time = LocalDateTime.ofEpochSecond(
                hourlyCondition.timeStamp.toLong(),
                0,
                ZoneOffset.ofTotalSeconds(hourlyCondition.timeZoneOffset)
            )
                .format(DateTimeFormatter.ofPattern("HH:mm"))

            itemView.text_view_condition_item_time.text = time
            itemView.text_view_condition_item_wind.text = hourlyCondition.windSpeed.toString()
            itemView.text_view_condition_item_volume.text = allVolume.toString()
            iconId?.let { itemView.image_view_condition_item.setImageResource(it) }
        }
    }

}