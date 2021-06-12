package com.example.simpleweather.ui.screens.conditiondetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.databinding.ItemDailyConditionBinding
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import com.example.simpleweather.utils.idToIcon
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class DailyConditionalAdapter :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val boundedView = ItemDailyConditionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyConditionalViewHolder(boundedView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dailyCondition = getItem(position) as DailyCondition
        (holder as DailyConditionalViewHolder).bind(dailyCondition)
    }

    inner class DailyConditionalViewHolder(private val binding: ItemDailyConditionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dailyCondition: DailyCondition) {
            val iconId = idToIcon(dailyCondition.weatherId, false)
            val tempMaxStr = "${dailyCondition.tempMax}°"
            val tempMinStr = "${dailyCondition.tempMin}°"
            val dayOfWeekStr = LocalDateTime.ofEpochSecond(
                dailyCondition.timeStamp.toLong(),
                0,
                ZoneOffset.ofTotalSeconds(dailyCondition.timeZoneOffset)
            )
                .format(DateTimeFormatter.ofPattern("EE"))

            binding.textViewDayOfWeek.text = dayOfWeekStr
            binding.textViewWeatherName.text = dailyCondition.weatherDescription
            binding.textViewDayMaxTemp.text = tempMaxStr
            binding.textViewDayMinTemp.text = tempMinStr
            binding.imageViewDayIcon.setImageResource(iconId)
        }
    }

}