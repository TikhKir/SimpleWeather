package com.example.simpleweather.ui.conditiondetails

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
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.github.aachartmodel.aainfographics.aachartcreator.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.condition_details_fragment.*

@AndroidEntryPoint
class ConditionDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ConditionDetailsFragment()
    }

    private lateinit var viewModel: ConditionDetailsViewModel
    private val hourlyAdapter = HourlyConditionalAdapter()
    private val navArgs: ConditionDetailsFragmentArgs by navArgs()

    private var fakeHourlyList = mutableListOf<HourlyWeatherCondition>()
    private lateinit var aaChartModel: AAChartModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.condition_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getNavGraphArgs()
        initChartModel()
        initRecyclers()
        initViewModel()
        initListeners()
    }

    private fun initChartModel() {
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .animationDuration(500)
            .animationType(AAChartAnimationType.EaseInOutExpo)
            .backgroundColor("#ffffff")
            .tooltipCrosshairs(true) // выделение точки окружностью
            .dataLabelsEnabled(true) //подписи к точкам
            .tooltipEnabled(false) // зетенение всех линий кроме выделенной
            .legendEnabled(false) // видимость легенды
            .margin(arrayOf(7F, 40F, 7F, 40F)) // todo: instead 40F needs half of width of Rec_Item
            .yAxisVisible(false) // видимость оси Х (и сетки)
            .xAxisVisible(false) // видимость оси У (и сетки)
            .markerSymbol(AAChartSymbolType.Circle) // тип Диаграммы
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank) // обводка точек
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("temp")
                        .data(arrayOf(0, 0))
                )
            )
        aa_chart_view.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun initListeners() {
        button.setOnClickListener {
            viewModel.initList()
//            aa_chart_view.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
//                arrayOf(
//                    AASeriesElement()
//                        .name("temp")
//                        .data(arrayOf(1,5,1,5,1))
//                )
//            )
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ConditionDetailsViewModel::class.java)
        viewModel.fakeListLiveData.observe(viewLifecycleOwner, Observer {
            fakeHourlyList = it.toMutableList()
            hourlyAdapter.submitList(it.toList())
            calculateViewsSize(it.size)
            transformToChartSeriesAndRefresh(it)
        })
    }

    private fun transformToChartSeriesAndRefresh(hourlyList: List<HourlyWeatherCondition>) {
        val seriesArray = arrayListOf<Float>()
        for (i in hourlyList) {
            i.temp?.let { seriesArray.add(it) }
        }

        aa_chart_view.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
            arrayOf(
                AASeriesElement()
                    .name("temp")
                    .data(seriesArray.toArray())
            ),
            true
        )
    }

    private fun initRecyclers() {
        recyclerView_hourly_condition.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_hourly_condition.adapter = hourlyAdapter
    }

    private fun getNavGraphArgs() {
        val tmp = "id:${navArgs.locationId} lat:${navArgs.latitude} lon: ${navArgs.longitude}"
        text_view_conditionDetails.text = tmp
    }

    private fun calculateViewsSize(arraySize: Int) {
        val params = aa_chart_view.layoutParams
        val screenWidthPx = resources.displayMetrics.widthPixels / 5
        if (params.width != screenWidthPx * arraySize) {
            params.width = screenWidthPx * arraySize
            aa_chart_view.layoutParams = params
        }
    }

}