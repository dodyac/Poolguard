package com.acxdev.poolguardapps.ui.chart

import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.toDateEpoch
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.Time
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.databinding.FragmentChartBinding
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.model.pool_response.Chart
import com.acxdev.poolguardapps.model.pool_response.ChartEzil
import com.acxdev.poolguardapps.model.pool_response.HashrateResponse
import com.acxdev.poolguardapps.model.pool_response.History
import com.acxdev.poolguardapps.model.pool_response.MinerResponse
import com.acxdev.poolguardapps.model.pool_response.NanopoolHashRateChart
import com.acxdev.poolguardapps.repository.List.sortByTime
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.util.applyStyle
import com.acxdev.poolguardapps.util.formatReadable
import com.acxdev.poolguardapps.util.lineDataSetFilledDrawableWithReadable
import com.acxdev.poolguardapps.util.toEpochHiveOnChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*

class FragmentShares : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {

    lateinit var valid: ILineDataSet
    lateinit var invalid: ILineDataSet
    lateinit var stale: ILineDataSet

    val times = mutableListOf<String>()

    override fun FragmentChartBinding.configureViews() {
        try {
            val dataset = mutableListOf<ILineDataSet>()

            sortBy(dataset, Time.ONE_DAY)

            safeContext {
                time.setHStack(RowTime(sortByTime(), object : OnClick<Menu> {
                    override fun onItemClick(item: Menu, position: Int) {
                        reportedHashrateCheck.isChecked = true
                        currentHashrateCheck.isChecked = true
                        averageHashrateCheck.isChecked = true
                        when(position){
                            0 -> sortBy(dataset, Time.ONE_DAY)
                            1 -> sortBy(dataset, Time.ONE_HOUR)
                            2 -> sortBy(dataset, Time.TWO_HOURS)
                            3 -> sortBy(dataset, Time.FIVE_HOURS)
                            4 -> sortBy(dataset, Time.TEN_HOURS)
                        }
                    }
                }), hasFixed = true)
            }

            reportedHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(valid) else dataset.remove(valid)
                chart.invalidate()
            }

            currentHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(invalid) else dataset.remove(invalid)
                chart.invalidate()
            }

            averageHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(stale) else dataset.remove(stale)
                chart.invalidate()
            }
        } catch (e: Exception){ empty() }

        reportedHashrateCheck.text = getString(R.string.valid)
        currentHashrateCheck.text = getString(R.string.invalid)
        averageHashrateCheck.text = getString(R.string.stale)
    }

    override fun FragmentChartBinding.onClickListener() {

    }

    class MyFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return value.formatReadable().removeSuffix("H/s")
        }
    }

    private fun sortBy(dataset: MutableList<ILineDataSet>, enum: Time) {
        scopeLayout {
            dataset.clear()
            when(getStringExtra(Constant.Intent.POOL)){
                Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                    val data = getExtraAs(Array<ChartEzil>::class.java).asList()

                    init(data.map { it.time.toEpochHiveOnChart() }, data.map { it.accepted_shares_count }, data.map { it.invalid_shares_count },
                        data.map { it.stale_shares_count }, enum)
                }
                Pool.HIVEON.value -> {
                    val data = getExtraAs(HashrateResponse::class.java)

                    init(data.shares.items.map { it.timestamp.toEpochHiveOnChart() }.reversed(), data.shares.items.map { it.validCount }.reversed(), emptyList(),
                        data.shares.items.map { it.staleCount }.reversed(), enum)
                }
                Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.shareCharts.map { it.x }.reversed(), data.shareCharts.map { it.accepted }.reversed(), data.shareCharts.map { it.invalid }.reversed(),
                        data.shareCharts.map { it.stale }.reversed(), enum)
                }
                Pool.NANOPOOL.value -> {
                    val data = getExtraAs(NanopoolHashRateChart::class.java)

                    init(data.data.sortedBy { it.date }.map { it.date }, data.data.sortedBy { it.date }.map { it.shares }, emptyList(), emptyList(), enum)

                    checkBoxLayout.gone()
                }
                Pool.FLEXPOOL.value -> {
                    val data = getExtraAs(Chart::class.java)

                    init(data.result.map { it.timestamp }, data.result.map { it.validShares }, data.result.map { it.invalidShares },
                        data.result.map { it.staleShares }, enum)
                }
                Pool.COMINERS.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.shareCharts.map { it.x }.reversed(), data.shareCharts.map { it.valid }.reversed(), emptyList(), data.shareCharts.map { it.stale }.reversed(), enum)
                }
                Pool.ETHERMINE.value, Pool.FLYPOOL.value -> {
                    val data = getExtraAs(History::class.java)

                    init(data.data.map { it.time!! }, data.data.map { it.validShares }, data.data.map { it.invalidShares }, data.data.map { it.staleShares }, enum)
                }
            }

            dataset.add(valid)
            dataset.add(invalid)
            dataset.add(stale)

            chart.applyStyle(times, dataset)
            chart.axisLeft.valueFormatter = MyFormatter()
        }
    }

    private fun empty() {
        scopeLayout {
            safeContext {
                checkBoxLayout.gone()
                checkBoxLayout.invalidate()
                chart.setNoDataTextColor(getColor(R.color.red))
            }
        }
    }

    private fun init(timeList: List<Long>, validList: List<Float?>, invalidList: List<Float?>, staleList: List<Float?>, enum: Time) {
        safeContext {
            valid = lineDataSetFilledDrawableWithReadable(getString(R.string.reported), validList.takeLast(enum.value), R.color.green, R.drawable.bg_fade_green)
            invalid = lineDataSetFilledDrawableWithReadable(getString(R.string.current), invalidList.takeLast(enum.value), R.color.primaryBlue, R.drawable.bg_fade_blue)
            stale = lineDataSetFilledDrawableWithReadable(getString(R.string.average), staleList.takeLast(enum.value), R.color.yellow, R.drawable.bg_fade_yellow)
            times.clear()
            times.addAll(timeList.map { it.toDateEpoch(Constant.HOUR_MINUTE, Locale.ENGLISH) }.takeLast(enum.value))
        }
    }
}