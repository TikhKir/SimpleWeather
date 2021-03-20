package com.example.simpleweather.ui.screens.conditiondetails

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.MainActivity
import com.example.simpleweather.R
import com.example.simpleweather.ui.model.CurrentConditionUI
import com.example.simpleweather.ui.model.HourlyConditionUI
import com.example.simpleweather.utils.asyncunits.DegreeUnits
import com.example.simpleweather.utils.asyncunits.PressureUnits
import com.example.simpleweather.utils.asyncunits.Units
import com.example.simpleweather.utils.asyncunits.WindSpeedUnits
import com.example.simpleweather.utils.datawrappers.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.condition_details_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var changeMenu: Menu
    private val navArgs: ConditionDetailsFragmentArgs by navArgs()
    private var widthOfItem = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        widthOfItem = resources.displayMetrics.widthPixels / numberOfColumns
        return inflater.inflate(R.layout.condition_details_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConditionDetailsViewModel::class.java)

        if (savedInstanceState == null) {
            startRequestWithNavGraphArgs()
            initFavouriteState()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        initRecyclers()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentLiveData.observe(viewLifecycleOwner, {
            initCurrentState(it)
        })
        viewModel.hourlyLiveData.observe(viewLifecycleOwner, {
            hourlyAdapter.submitList(it)
            initChart(it)
            calculateViewsSize(it.size)
        })
        viewModel.dailyLivaData.observe(viewLifecycleOwner, {
            dailyAdapter.submitList(it)
        })
        viewModel.stateLiveData.observe(viewLifecycleOwner, {
            setLoadingState(it)
        })
    }

    private fun initCurrentState(currentCondition: CurrentConditionUI) = with(currentCondition) {
            val tempStr = "$temp" + getString(R.string.sign_degree)
            val humidityStr = "$humidity" + getString(R.string.sign_percent)
            val pressureStr = "$pressure ${provideUnitsString(pressureUnits)}"
            val windSpeedStr = "$windSpeed ${provideUnitsString(windSpeedUnits)}"
            val allVolumeStr = "$allVolumeLastHour " + getString(R.string.units_pressure_mm)

            val feelsLikeStr = getString(R.string.feels_like) +
                    "$tempFL${provideUnitsString(tempUnits)}"

            val timeStr = LocalDateTime.ofEpochSecond(timeStamp.toLong(), 0,
            ZoneOffset.ofTotalSeconds(timeZoneOffset))
                .format(DateTimeFormatter.ofPattern("EEEE, d MMMM, HH:mm"))

            //todo: подтягивать информацию о локации из репозитория через flow
            val maxRefresh = maxOf(
                navArgs.location.refreshTimeCurrent,
                navArgs.location.refreshTimeHourly
            )

            val refreshTimeStr = getString(R.string.updated) +
                    LocalDateTime.ofEpochSecond(maxRefresh, 0,
                ZoneOffset.ofTotalSeconds(timeZoneOffset)
            ).format(DateTimeFormatter.ofPattern("d MMMM, HH:mm"))

            text_view_current_datetime.text = timeStr
            text_view_refresh_time.text = refreshTimeStr
            text_view_current_temperature.text = tempStr
            text_view_current_conditional.text = weatherDescription
            text_view_current_feelslike.text = feelsLikeStr

            text_view_hudimity_count.text = humidityStr
            text_view_pressure_count.text = pressureStr
            text_view_wind_count.text = windSpeedStr
            text_view_volume_prec_count.text = allVolumeStr
    }

    private fun initChart(hourlyList: List<HourlyConditionUI>) {
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

    private fun transformToPointsList(hourlyList: List<HourlyConditionUI>): List<PointValue> {
        val pointsList = mutableListOf<PointValue>()
        for ((index, value) in hourlyList.withIndex()) {
            pointsList.add(PointValue(index.toFloat(), value.temp.toFloat()))
        }
        return pointsList
    }

    private fun calculateViewsSize(arraySize: Int) {
        //matching the size of graph to the size of recycler to fit in one scrollview
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

    @ExperimentalCoroutinesApi
    private fun startRequestWithNavGraphArgs() {
        val location = navArgs.location
        if (location.locationId != -1L) { //location already exist in db //todo: это плохо
            viewModel.getCurrentWeatherCondition(location.locationId)
            viewModel.getHourlyWeatherCondition(location.locationId)
            viewModel.getDailyWeatherCondition(location.locationId)
        } else {
            viewModel.getCurrentWeatherCondition(location.latitude, location.longitude)
            viewModel.getHourlyWeatherCondition(location.latitude, location.longitude)
            viewModel.getDailyWeatherCondition(location.latitude, location.longitude)
        }
    }


    private fun setAsFavourite() {
        viewModel.isFavourite = true
        changeMenu.findItem(R.id.action_save).isVisible = false
        changeMenu.findItem(R.id.action_delete).isVisible = true
        Toast.makeText(requireContext(), getString(R.string.added_to_favourite), Toast.LENGTH_SHORT)
            .show()
    }

    private fun unsetAsFavourite() {
        viewModel.isFavourite = false
        changeMenu.findItem(R.id.action_save).isVisible = true
        changeMenu.findItem(R.id.action_delete).isVisible = false
        Toast.makeText(
            requireContext(),
            getString(R.string.deleted_from_favourite),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setLoadingState(state: State) {
        when (state) {
            is State.Default -> setLoading(true)
            is State.Loading -> setLoading(true)
            is State.Error -> showErrorMessage(state.errorMessage)
            is State.Success -> setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        condition_progress_bar.isVisible = isLoading
        condition_fragment_root.isVisible = !isLoading
        condition_error_message.isVisible = false
    }

    private fun showErrorMessage(message: String) {
        condition_progress_bar.isVisible = false
        condition_fragment_root.isVisible = false
        condition_error_message.text = message
        condition_error_message.isVisible = true
    }

    private fun initFavouriteState() {
        viewModel.isFavourite = (navArgs.location.locationId != -1L)
        viewModel.favouriteLocation = navArgs.location
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        changeMenu = menu
        inflater.inflate(R.menu.conditional_favourite_menu, menu)

        if (viewModel.isFavourite) {
            changeMenu.findItem(R.id.action_save).isVisible = false
            changeMenu.findItem(R.id.action_delete).isVisible = true
        } else {
            changeMenu.findItem(R.id.action_save).isVisible = true
            changeMenu.findItem(R.id.action_delete).isVisible = false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                setAsFavourite()
            }
            R.id.action_delete -> {
                unsetAsFavourite()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setActionBarTitle(navArgs.location.addressCity)
    }


    private fun provideUnitsString(unit: Units): String = when (unit) {
        is PressureUnits.MillimetersOfMercury -> getString(R.string.units_pressure_mm)
        is PressureUnits.HectoPascals -> getString(R.string.units_pressure_hpa)
        is DegreeUnits.Celsius -> getString(R.string.units_dergee_celsius)
        is DegreeUnits.Fahrenheit -> getString(R.string.units_degree_fahrenheit)
        is WindSpeedUnits.MetersPerSecond -> getString(R.string.units_wind_speed_meters_per_second)
        is WindSpeedUnits.KilometersPerHour -> getString(R.string.units_wind_speed_kilometers_per_hours)
    }


}