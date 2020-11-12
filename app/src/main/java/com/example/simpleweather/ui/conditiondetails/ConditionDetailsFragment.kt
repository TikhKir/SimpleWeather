package com.example.simpleweather.ui.conditiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.simpleweather.R
import com.github.aachartmodel.aainfographics.aachartcreator.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.condition_details_fragment.*

@AndroidEntryPoint
class ConditionDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ConditionDetailsFragment()
    }

    private lateinit var viewModel: ConditionDetailsViewModel
    private val navArgs: ConditionDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.condition_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConditionDetailsViewModel::class.java)

        val tmp = "id:${navArgs.locationId} lat:${navArgs.latitude} lon: ${navArgs.longitude}"
        text_view_conditionDetails.text = tmp


        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Spline)
            .animationDuration(1000)

            .animationType(AAChartAnimationType.EaseInOutExpo)
            .backgroundColor("#ffffff")
            .tooltipCrosshairs(true) // выделение точки окружностью
            .dataLabelsEnabled(true) //подписи к точкам
            .tooltipEnabled(false) // зетенение всех линий кроме выделенной
            .legendEnabled(false) // видимость легенды
            .yAxisVisible(false) // видимость оси Х (и сетки)
            .xAxisVisible(false) // видимость оси У (и сетки)
            //.yAxisMax(21F)
            .markerSymbol(AAChartSymbolType.Circle) // тип Диаграммы
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank) // обводка точек
            .series(arrayOf(
                AASeriesElement()
                    .name("Температура")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 13.2, 7.5)),
//                AASeriesElement()
//                    .name("Влажность")
//                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 10.0, 8.0))
            )
            )

        aa_chart_view.aa_drawChartWithChartModel(aaChartModel)
    }

}