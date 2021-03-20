package com.example.simpleweather.ui.screens.conditiondetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.R
import com.example.simpleweather.ui.model.HourlyConditionUI
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import com.example.simpleweather.utils.iconconverter.IconConverter
import com.example.simpleweather.utils.toUIFormat
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
        val hourlyCondition = getItem(position) as HourlyConditionUI
        (holder as HourlyConditionalViewHolder).bind(hourlyCondition)
    }

    inner class HourlyConditionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hourlyCondition: HourlyConditionUI) = with(hourlyCondition) {
            val allVolumeStr = (rainVolume + snowVolume).toUIFormat()
            val windSpeedStr = windSpeed.toUIFormat()
            val localDateTime = LocalDateTime.ofEpochSecond(
                timeStamp.toLong(),
                0,
                ZoneOffset.ofTotalSeconds(timeZoneOffset)
            )
            val timeStr = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val iconId = IconConverter.idToIcon(weatherId, isNight(localDateTime))

            itemView.text_view_condition_item_time.text = timeStr
            itemView.text_view_condition_item_wind.text = windSpeedStr
            itemView.text_view_condition_item_volume.text = allVolumeStr
            itemView.image_view_condition_item.setImageResource(iconId)
        }
    }

    //very sad that in this object I do not have access to the time of sunset and sunrise
    private fun isNight(localDateTime: LocalDateTime): Boolean =
        ((localDateTime.hour < 6) || (localDateTime.hour > 22))

}