package com.acxdev.poolguardapps.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.adapter.ViewPager2Adapter
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.putExtra
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.setWrapContent
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.databinding.LayoutViewPagerChartBinding
import com.acxdev.poolguardapps.databinding.LayoutViewPagerChartShimmerBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.ui.chart.FragmentHashRate
import com.acxdev.poolguardapps.ui.chart.FragmentShares
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import java.math.BigDecimal

fun <T> AppCompatActivity.initChartHashShares(
    chartShimmerBinding: LayoutViewPagerChartShimmerBinding,
    chartBinding: LayoutViewPagerChartBinding,
    wallet: Wallet,
    data: T
) {
    chartShimmerBinding.root.gone()
    chartBinding.root.visible()
    val pager2Adapter = ViewPager2Adapter(this)
    val type =
        if (wallet.symbol == Crypto.XCH.name) getString(R.string.space) else getString(R.string.hashrate)
    val mutableListFragment = mutableListOf<Pair<String, Fragment>>()
    try {
        //TODO Raw String
        mutableListFragment.add(
            Pair(
                type,
                FragmentHashRate().putExtra(
                    Gson().toJson(data),
                    Constant.Intent.POOL,
                    wallet.pool,
                    "isChia",
                    wallet.symbol
                )
            )
        )
        mutableListFragment.add(
            Pair(
                getString(R.string.shares),
                FragmentShares().putExtra(
                    Gson().toJson(data),
                    Constant.Intent.POOL,
                    wallet.pool
                )
            )
        )
    } catch (e: Exception) {
        mutableListFragment.add(Pair(type, FragmentHashRate()))
        mutableListFragment.add(Pair(getString(R.string.shares), FragmentShares()))
    }
    chartBinding.viewPager.adapter = pager2Adapter
    pager2Adapter.setWithTab(
        mutableListFragment,
        chartBinding.tabLayout,
        chartBinding.viewPager
    )
    chartBinding.tabLayout.setTabTextColors(getColorPrimary(), getColor(com.acxdev.commonFunction.R.color.white))
    chartBinding.viewPager.setWrapContent()
}

fun LineChart.applyStyle(
    indexAxisValueFormatter: List<String>,
    dataset: MutableList<ILineDataSet>
) {
    legend.apply {
        isEnabled = false
        verticalAlignment = Legend.LegendVerticalAlignment.TOP
        horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        orientation = Legend.LegendOrientation.HORIZONTAL
        setDrawInside(false)
    }
    xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(indexAxisValueFormatter)
        granularity = 1F
        isGranularityEnabled = true
        textColor = context.getColor(R.color.text)
        setDrawGridLines(false)
        position = XAxis.XAxisPosition.BOTTOM
    }
    axisLeft.apply {
        textColor = context.getColor(R.color.text)
        enableGridDashedLine(10F, 10F, 0F)
        setDrawAxisLine(false)
    }
    axisRight.apply {
        isEnabled = false
        textColor = context.getColor(R.color.text)
        enableGridDashedLine(10F, 10F, 0F)
        setDrawAxisLine(false)
    }

    animateX(600, Easing.Linear)
    data = LineData(dataset)
    description.isEnabled = false
    invalidate()
}

fun LineChart.applyStyleDisabled(
    indexAxisValueFormatter: List<String>,
    dataset: MutableList<ILineDataSet>
) {
    applyStyle(indexAxisValueFormatter, dataset)
    axisRight.isEnabled = false
    axisLeft.isEnabled = false
    xAxis.isEnabled = false
    setViewPortOffsets(0f, 0f, 0f, 0f)
    setTouchEnabled(false)
    setScaleEnabled(false)
    isDragEnabled = false
    isScaleXEnabled = false
    isScaleYEnabled = false
    setPinchZoom(false)
    invalidate()
}

fun BarChart.applyStyle(
    indexAxisValueFormatter: List<String>,
    dataset: MutableList<IBarDataSet>
) {
    legend.apply {
        isEnabled = false
        verticalAlignment = Legend.LegendVerticalAlignment.TOP
        horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        orientation = Legend.LegendOrientation.HORIZONTAL
        setDrawInside(false)
    }
    xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(indexAxisValueFormatter)
        granularity = 1F
        isGranularityEnabled = true
        textColor = context.getColor(R.color.text)
        position = XAxis.XAxisPosition.BOTTOM
        labelCount = indexAxisValueFormatter.size
        setDrawGridLines(false)
    }
    axisLeft.apply {
        textColor = context.getColor(R.color.text)
        enableGridDashedLine(10F, 10F, 0F)
        setDrawAxisLine(false)
        axisMinimum = 0F
    }
    axisRight.apply {
        textColor = context.getColor(R.color.text)
        isEnabled = false
        setDrawAxisLine(false)
    }

    animateX(600, Easing.Linear)

    setScaleEnabled(false)
    data = BarData(dataset)
    data.barWidth = 0.4F
    description.isEnabled = false
    invalidate()
}

fun Context.barDataSet(
    label: String,
    list: List<BigDecimal?>,
    color: Int
): IBarDataSet {
    val entry = mutableListOf<BarEntry>()
    list.forEachIndexed { index, bigDecimal ->
        entry.add(BarEntry(index.toFloat(),
            try {
                bigDecimal!!.toFloat()
            } catch (e: Exception) {
                0F
            })
        )
    }
    val lineDataSet = BarDataSet(entry, label)
    lineDataSet.color = getColor(color)
    lineDataSet.valueTextSize = 0F
    lineDataSet.highLightColor = getColor(R.color.primaryBlue)
    lineDataSet.highLightAlpha = 255
    return lineDataSet
}

fun Context.lineDataSetFilledDrawableWithReadable(
    label: String,
    list: List<Float?>,
    color: Int,
    filledColor: Int
): ILineDataSet {
    val entry = mutableListOf<Entry>()
    list.forEachIndexed { index, bigDecimal ->
        entry.add(Entry(index.toFloat(),
            try {
                bigDecimal!!.toFloat()
            } catch (e: Exception) {
                0F
            })
        )
    }
    val lineDataSet = LineDataSet(entry, label)
    lineDataSet.color = getColor(color)
    lineDataSet.lineWidth = 2F
    lineDataSet.setDrawCircles(false)
    lineDataSet.fillDrawable = AppCompatResources.getDrawable(this, filledColor)
    lineDataSet.setDrawFilled(true)
    lineDataSet.valueTextSize = 0F
    lineDataSet.highLightColor = getColor(android.R.color.transparent)
    lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
    return lineDataSet
}

fun Context.lineDataSetFilledColor(
    label: String,
    list: List<Double>,
    color: Int,
    filledColor: Int
): ILineDataSet {
    val entry = mutableListOf<Entry>()
    list.forEachIndexed { index, d ->
        entry.add(Entry(index.toFloat(), d.toFloat()))
    }
    val lineDataSet = LineDataSet(entry, label)
    lineDataSet.color = getColor(color)
    lineDataSet.lineWidth = 2F
    lineDataSet.setDrawCircles(false)
    lineDataSet.fillColor = getColor(filledColor)
    lineDataSet.setDrawFilled(true)
    lineDataSet.valueTextSize = 0F
    lineDataSet.highLightColor = getColor(android.R.color.transparent)
    lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
    return lineDataSet
}