package com.example.simpleweather.ui.screens.conditiondetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.databinding.ItemHourlyConditionBinding
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import com.example.simpleweather.utils.idToIcon
import com.example.simpleweather.utils.toUIFormat
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class HourlyConditionalAdapter(private val itemWidth: Int) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val boundedView =
            ItemHourlyConditionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //set custom width of item
        val tempParam = boundedView.root.layoutParams
        tempParam.width = itemWidth
        boundedView.root.layoutParams = tempParam

        return HourlyConditionalViewHolder(boundedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hourlyCondition = getItem(position) as HourlyCondition
        (holder as HourlyConditionalViewHolder).bind(hourlyCondition)
    }

    inner class HourlyConditionalViewHolder(private val binding: ItemHourlyConditionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(hourlyCondition: HourlyCondition) = with(hourlyCondition) {
            val allVolumeStr = (rainVolume + snowVolume).toUIFormat()
            val windSpeedStr = windSpeed.toUIFormat()
            val localDateTime = LocalDateTime.ofEpochSecond(
                timeStamp.toLong(),
                0,
                ZoneOffset.ofTotalSeconds(timeZoneOffset)
            )
            val timeStr = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val iconId = idToIcon(weatherId, isNight(localDateTime))

            binding.textViewConditionItemTime.text = timeStr
            binding.textViewConditionItemWind.text = windSpeedStr
            binding.textViewConditionItemVolume.text = allVolumeStr
            binding.imageViewConditionItem.setImageResource(iconId)
        }
    }

    //very sad that in this object I do not have access to the time of sunset and sunrise
    private fun isNight(localDateTime: LocalDateTime): Boolean =
        ((localDateTime.hour < 6) || (localDateTime.hour > 22))

}