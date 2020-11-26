package com.example.simpleweather.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simpleweather.R
import kotlinx.android.synthetic.main.settings_fragment.*
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initMPChart()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    private fun initMPChart() {
        val fakeList = listOf<PointValue>(
            PointValue(0F, 2F),
            PointValue(1F, 5F),
            PointValue(2F, 10F),
            PointValue(3F, 1F),
            PointValue(4F, 7F),
        )

        val line = Line(fakeList)
            .setColor(R.color.colorPrimary)
            .setCubic(true)
            .setFilled(true)   // закрашивание области под линией
            .setStrokeWidth(2) //ширина линии (непонятно в чем)
            .setHasLabels(true) //подписи к точкам


        val lines = arrayListOf<Line>()
        lines.add(line)

        val lineChartData = LineChartData(lines)


        val v = Viewport(chart.maximumViewport)
        v.top = 11F
        v.right = 4.2F
        v.bottom = 0F
        v.left = -0.2F
        Log.e("VIEWPORT_1", "${v.top} ${v.right} ${v.bottom} ${v.left}")


        chart.maximumViewport = v
        chart.setCurrentViewportWithAnimation(v)
        chart.isViewportCalculationEnabled = false
        chart.lineChartData = lineChartData

        chart.isZoomEnabled = false


//        chart.startDataAnimation(1000) //Обновить данные с анимацией


    }

}