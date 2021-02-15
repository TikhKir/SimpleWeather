package com.example.simpleweather.ui.conditiondetails

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.MainActivity
import com.example.simpleweather.R
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.datawrappers.State
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
        viewModel.currentLiveData.observe(viewLifecycleOwner, Observer {
            initCurrentState(it)
        })
        viewModel.hourlyLiveData.observe(viewLifecycleOwner, Observer {
            hourlyAdapter.submitList(it.toList())
            initChart(it)
            calculateViewsSize(it.size)
        })
        viewModel.dailyLivaData.observe(viewLifecycleOwner, Observer {
            dailyAdapter.submitList(it.toList())
        })
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            setLoadingState(it)
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
        ).format(DateTimeFormatter.ofPattern("EEEE, d MMMM, HH:mm"))

        //todo: подтягивать информацию о локации из репозитория через flow
        val refreshTime = LocalDateTime.ofEpochSecond(
            navArgs.location.refreshTimeCurrent,
            0,
            ZoneOffset.ofTotalSeconds(currentCondition.timeZoneOffset)
        ).format(DateTimeFormatter.ofPattern("d MMMM, HH:mm"))

        text_view_current_datetime.text = time
        text_view_refresh_time.text = refreshTime
        text_view_current_temperature.text = currentCondition.temp?.toInt().toString()
        text_view_current_conditional.text = currentCondition.weatherDescription
        text_view_current_feelslike.text = feelsLikeStr

        text_view_hudimity_count.text = currentCondition.humidity.toString()
        text_view_pressure_count.text = currentCondition.pressure.toString()
        text_view_wind_count.text = currentCondition.windSpeed.toString()
        val allVolume = (currentCondition.rainVolumeLastHour ?: 0F) +
                (currentCondition.snowVolumeLastHour ?: 0F)
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

    private fun startRequestWithNavGraphArgs() {
        val location = navArgs.location
        if (location.locationId != -1L) { //location already exist in db
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


}