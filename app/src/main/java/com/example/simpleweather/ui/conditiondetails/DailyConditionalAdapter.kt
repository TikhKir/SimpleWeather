package com.example.simpleweather.ui.conditiondetails


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.utils.diffutil.Identified
import com.example.simpleweather.utils.diffutil.IdentityDiffUtilCallback
import com.example.simpleweather.utils.iconconverter.IconConverter
import kotlinx.android.synthetic.main.item_daily_condition.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class DailyConditionalAdapter:
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_condition, parent, false)
        return DailyConditionalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dailyCondition = getItem(position) as DailyWeatherCondition
        (holder as DailyConditionalViewHolder).bind(dailyCondition)
    }

    inner class DailyConditionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dailyCondition: DailyWeatherCondition) {
            val iconId = dailyCondition.weatherId?.let { IconConverter.idToIcon(it, false) }

            val dayOfWeek = LocalDateTime.ofEpochSecond(
                dailyCondition.timeStamp.toLong(),
                0,
                ZoneOffset.ofTotalSeconds(dailyCondition.timeZoneOffset)
            )
                .format(DateTimeFormatter.ofPattern("EE"))

            itemView.text_view_day_of_week.text = dayOfWeek
            itemView.text_view_weather_name.text = dailyCondition.weatherDescription
            itemView.text_view_day_max_temp.text = dailyCondition.tempMax?.toInt().toString()
            itemView.text_view_day_min_temp.text = dailyCondition.tempMin?.toInt().toString()
            iconId?.let { itemView.image_view_day_icon.setImageResource(it) }
        }
    }

}