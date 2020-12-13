package com.example.simpleweather.ui.conditiondetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.condition_details_fragment.*
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class ConditionDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ConditionDetailsFragment()
        private const val numberOfColumns = 5
    }

    private lateinit var viewModel: ConditionDetailsViewModel
    private lateinit var hourlyAdapter: HourlyConditionalAdapter
    private lateinit var dailyAdapter: DailyConditionalAdapter
    private val navArgs: ConditionDetailsFragmentArgs by navArgs()

    private var fakeHourlyList = mutableListOf<HourlyWeatherCondition>()
    private var widthOfItem = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        widthOfItem = resources.displayMetrics.widthPixels / numberOfColumns
        return inflater.inflate(R.layout.condition_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclers()
        initViewModel()
        initListeners()
        if (savedInstanceState == null) getNavGraphArgs()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ConditionDetailsViewModel::class.java)
        viewModel.currentLiveData.observe(viewLifecycleOwner, Observer {
            initCurrentState(it)
        })
        viewModel.hourlyListLiveData.observe(viewLifecycleOwner, Observer {
            //fakeHourlyList = it.toMutableList()
            hourlyAdapter.submitList(it.toList())
            initChart(it)
            calculateViewsSize(it.size)
        })
        viewModel.dailyListLiveData.observe(viewLifecycleOwner, Observer {
            dailyAdapter.submitList(it.toList())
        })
    }

    private fun initCurrentState(currentCondition: CurrentWeatherCondition) {
        //todo: нужн сделать класс для преобразования единиц измерения,
        // в который при передаче значения будет возвращаться рассчитанная
        // строка с приконкатенированными единицами

        val feelsLikeStr =
            getString(R.string.feels_like) + currentCondition.tempFL?.toInt().toString()
        val time = LocalDateTime.ofEpochSecond(
            currentCondition.timeStamp.toLong(),
            0,
            ZoneOffset.ofTotalSeconds(currentCondition.timeZoneOffset)
        )
            .format(DateTimeFormatter.ofPattern("EEEE, d MMMM, HH:mm"))

        text_view_current_datetime.text = time
        text_view_current_temperature.text = currentCondition.temp?.toInt().toString()
        text_view_current_conditional.text = currentCondition.weatherDescription
        text_view_current_feelslike.text = feelsLikeStr

        text_view_hudimity_count.text = currentCondition.humidity.toString()
        text_view_pressure_count.text = currentCondition.pressure.toString()
        text_view_wind_count.text = currentCondition.windSpeed.toString()
        val allVolume = currentCondition.rainVolumeLastHour ?: 0
        text_view_volume_prec_count.text = allVolume.toString()
    }

    private fun initChart(hourlyList: List<HourlyWeatherCondition>) {
        val pointsList = transformToPointsList(hourlyList)

        val line = Line(pointsList)
            .setColor(Color.MAGENTA)
            .setPointColor(Color.BLUE)
            .setCubic(true)
            .setFilled(false)   //закрашивание области под линией
            .setStrokeWidth(4)  //ширина линии (непонятно в чем)
            .setHasLabels(true) //подписи к точкам

        val lineList = arrayListOf<Line>(line)
        val lineChartData = LineChartData(lineList)

        hello_chart_view.isZoomEnabled = false
        hello_chart_view.isViewportCalculationEnabled = true
        hello_chart_view.lineChartData = lineChartData
    }

    private fun transformToPointsList(hourlyList: List<HourlyWeatherCondition>): List<PointValue> {
        val pointsList = mutableListOf<PointValue>()
        for ((index, value) in hourlyList.withIndex()) {
            pointsList.add(PointValue(index.toFloat(), value.temp!!.toFloat()))
        }
        return pointsList
    }

    private fun calculateViewsSize(arraySize: Int) {
        val params = hello_chart_view.layoutParams

        if (params.width != widthOfItem * arraySize) {
            params.width = widthOfItem * arraySize
            hello_chart_view.layoutParams = params

            val vp = Viewport(hello_chart_view.maximumViewport)
            val newVp = Viewport()
            val vDeltaOffset = (vp.top - vp.bottom) * 0.1F
            val hDeltaOffset = 0.38F

            newVp.top = vp.top + vDeltaOffset
            newVp.bottom = vp.bottom - vDeltaOffset
            newVp.right = vp.right + hDeltaOffset
            newVp.left = vp.left - hDeltaOffset

            hello_chart_view.maximumViewport = newVp
            hello_chart_view.currentViewport = newVp
        }
        hello_chart_view.invalidate()
    }


    private fun initListeners() {}

    private fun initRecyclers() {
        hourlyAdapter = HourlyConditionalAdapter(widthOfItem)
        recyclerView_hourly_condition.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_hourly_condition.adapter = hourlyAdapter

        dailyAdapter = DailyConditionalAdapter()
        recyclerView_daily_conditions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView_daily_conditions.adapter = dailyAdapter
    }

    private fun getNavGraphArgs() {
        if (navArgs.locationId != -1L) { //location already exist in db
            viewModel.getCurrentWeatherCondition(navArgs.locationId)
            viewModel.getHourlyWeatherCondition(navArgs.locationId)
            viewModel.getDailyWeatherCondition(navArgs.locationId)
        } else {
            viewModel.getCurrentWeatherCondition(navArgs.latitude, navArgs.longitude)
            viewModel.getHourlyWeatherCondition(navArgs.latitude, navArgs.longitude)
            viewModel.getDailyWeatherCondition(navArgs.latitude, navArgs.longitude)
        }
    }


}