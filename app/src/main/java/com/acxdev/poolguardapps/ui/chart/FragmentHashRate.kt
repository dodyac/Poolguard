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
import com.acxdev.poolguardapps.model.pool_response.FlockpoolDashboard
import com.acxdev.poolguardapps.model.pool_response.HashrateResponse
import com.acxdev.poolguardapps.model.pool_response.HeroMinersResponse
import com.acxdev.poolguardapps.model.pool_response.History
import com.acxdev.poolguardapps.model.pool_response.K1PoolResponse
import com.acxdev.poolguardapps.model.pool_response.LuckPoolHashrateResponse
import com.acxdev.poolguardapps.model.pool_response.MineXmrHashrateChart
import com.acxdev.poolguardapps.model.pool_response.MinerResponse
import com.acxdev.poolguardapps.model.pool_response.MinerpoolResponse
import com.acxdev.poolguardapps.model.pool_response.NanopoolHashRateChart
import com.acxdev.poolguardapps.model.pool_response.RavenMiner
import com.acxdev.poolguardapps.model.pool_response.TwoMinersChart
import com.acxdev.poolguardapps.model.pool_response.UnMineableWorker
import com.acxdev.poolguardapps.model.pool_response.WoolyPoolyResponse
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.List.sortByTime
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.util.applyStyle
import com.acxdev.poolguardapps.util.fixHashRateUnMineable
import com.acxdev.poolguardapps.util.formatReadable
import com.acxdev.poolguardapps.util.lineDataSetFilledDrawableWithReadable
import com.acxdev.poolguardapps.util.toEpoch
import com.acxdev.poolguardapps.util.toEpochHiveOnChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*

class FragmentHashRate : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {

    lateinit var reported: ILineDataSet
    lateinit var current: ILineDataSet
    lateinit var average: ILineDataSet

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
                }))
            }

            reportedHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(reported) else dataset.remove(reported)
                chart.invalidate()
            }

            currentHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(current) else dataset.remove(current)
                chart.invalidate()
            }

            averageHashrateCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) dataset.add(average) else dataset.remove(average)
                chart.invalidate()
            }
        } catch (e: Exception){
            empty()
            e.printStackTrace()
        }
    }

    override fun FragmentChartBinding.onClickListener() {

    }

    private fun sortBy(dataset: MutableList<ILineDataSet>, enum: Time){
        dataset.clear()
        scopeLayout {
            when(getStringExtra(Constant.Intent.POOL)) {
                Pool.FLOCKPOOL.value -> {
                    val data = getExtraAs(FlockpoolDashboard::class.java)

                    empty()
                    init(emptyList(), emptyList(), data.hashrate_graph.hashrate.map { it.toFloat() }, emptyList(), enum)
                }
                Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> {
                    val data = getExtraAs(MinerpoolResponse::class.java)

                    init(data.history.map { it.t }, emptyList(), data.history.map { it.h.toFloat() }, data.history.map { it.havg2h.toFloat() }, enum)
                    reportedHashrateCheck.gone()
                }
                Pool.UNMINEABLE.value -> {
                    val data = getExtraAs(UnMineableWorker::class.java)

                    val etHash = data.data.ethash.chart
                    val etcHash = data.data.etchash.chart
                    val randomX = data.data.randomx.chart
                    val kawpow = data.data.kawpow.chart

                    averageHashrateCheck.gone()

                    init(etHash.reported.timestamps, etHash.reported.data.map { it.fixHashRateUnMineable("SHIB") },
                        etHash.calculated.data.map { it.fixHashRateUnMineable("SHIB") }, emptyList(), enum)
                }
                Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                    val data = getExtraAs(Array<ChartEzil>::class.java).asList()

                    init(data.map { it.time.toEpochHiveOnChart() }, data.map { it.reported_hashrate }, data.map { it.short_average_hashrate },
                        data.map { it.long_average_hashrate }, enum)
                }
                Pool.HIVEON.value -> {
                    val data = getExtraAs(HashrateResponse::class.java)

                    init(data.items.map { it.timestamp.toEpochHiveOnChart() }.reversed(), data.items.map { it.reportedHashrate }.reversed(), data.items.map { it.hashrate }.reversed(),
                        data.items.map { it.meanHashrate }.reversed(), enum)
                }
                Pool.RAVENMINER.value -> {
                    val data = getExtraAs(RavenMiner::class.java)

                    empty()

                    init(data.hashrate.stats24H.map { it.t.toEpoch() }, emptyList(), data.hashrate.stats24H.map { it.hr }, emptyList(), enum)
                }
                Pool.COMINERS.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.minerCharts.map { it.x }.reversed(), emptyList(), data.minerCharts.map{ it.minerHash }.reversed(), data.minerCharts.map{ it.minerLargeHash }.reversed(), enum)
                }
                Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.minerCharts.map { it.x }, data.minerCharts.map{ it.minerReportedHash }, data.minerCharts.map{ it.minerHash }, data.minerCharts.map{ it.minerLargeHash }, enum)
                }
                Pool.MINEXMR.value -> {
                    val data = getExtraAs(MineXmrHashrateChart::class.java)

                    reportedHashrateCheck.gone()

                    init(data.time, emptyList(), data.hashrate, data.hashrateHourAverage, enum)
                }
                Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.minerCharts.map { it.x }.reversed(), data.minerCharts.map{ it.reportedHash }.reversed(), data.minerCharts.map{ it.minerHash }.reversed(),
                        data.minerCharts.map{ it.minerLargeHash }.reversed(), enum)
                }
                Pool.WOOLYPOOLYSOLO.value -> woolyPoolyGroup(enum, true)
                Pool.SOLOPOOL.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    empty()

                    init(data.charts.map { it.x }.reversed(), emptyList(), data.charts.map { it.y }.reversed(), emptyList(), enum)
                }
                Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> {
                    val data = getExtraAs(K1PoolResponse::class.java)

                    reportedHashrateCheck.gone()

                    init(data.miner.minerCharts.map { it.x }, emptyList(), data.miner.minerCharts.map{ it.minerHash }, data.miner.minerCharts.map{ it.minerLargeHash }, enum)
                }
                Pool.LUCKPOOL.value -> {
                    val data = getExtraAs(Array<LuckPoolHashrateResponse>::class.java).asList()

                    val reportedList = data.map { it.a[1] }
                    val currentList = data.map { it.a[2] }
                    val averageList = data.map { it.a[4 ] }
                    val timestamp = data.map { it.a[0].toLong() }

                    init(timestamp, reportedList, currentList, averageList, enum)
                }
                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                    try {
                        val data = getExtraAs(MinerResponse::class.java)

                        reportedHashrateCheck.gone()

                        init(data.minerCharts.map { it.x }.reversed(), emptyList(), data.minerCharts.map{ it.minerHash }.reversed(), data.minerCharts.map{ it.minerLargeHash }.reversed(), enum)
                    } catch (e: Exception) {
                        val data = getExtraAs(TwoMinersChart::class.java)

                        empty()

                        init(data.actual.map { it.x }.reversed(), emptyList(), data.actual.map{ it.y }.reversed(), emptyList(), enum)
                    }
                }
                Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    empty()

                    init(data.hashrateHistory.filter { it.label == "Total" }.map { it.time }, emptyList(), data.hashrateHistory.filter { it.label == "Total" }.map{ it.hr }, emptyList(), enum)
                }
                Pool.WOOLYPOOLY.value -> woolyPoolyGroup(enum)
                Pool.CRAZYPOOL.value -> {
                    val data = getExtraAs(MinerResponse::class.java)

                    init(data.minerCharts.map { it.x }, data.minerCharts.map{ it.minerReportedHash }, data.minerCharts.map{ it.minerHash }, data.minerCharts.map{ it.minerLargeHash }, enum)
                }
                Pool.HEROMINERS.value ->{
                    val data = getExtraAs(HeroMinersResponse::class.java)

                    empty()

                    init(data.charts.hashrate.map { it[0].toLong() }, emptyList(), data.charts.hashrate.map { it[1] }, emptyList(), enum)
                }
                Pool.NANOPOOL.value ->{
                    val data = getExtraAs(NanopoolHashRateChart::class.java)

                    val reportedList = data.data.sortedBy { it.date }.map { it.hashrate }
                    val currentList = data.data.sortedBy { it.date }.map { it.current }
                    val timestamp = data.data.sortedBy { it.date }.map { it.date }

                    averageHashrateCheck.gone()

                    init(timestamp, reportedList, currentList, emptyList(), enum)
                }
                Pool.FLEXPOOL.value ->{
                    val data = getExtraAs(Chart::class.java)

                    val reportedList = data.result.map { it.reportedHashrate }
                    val currentList = data.result.map { it.effectiveHashrate }
                    val averageList = data.result.map { it.averageEffectiveHashrate }
                    val timestamp = data.result.map { it.timestamp }

                    init(timestamp, reportedList, currentList, averageList, enum)
                }
                Pool.ETHERMINE.value, Pool.FLYPOOL.value -> {
                    val data = getExtraAs(History::class.java)

                    init(data.data.map { it.time!! }, data.data.map { it.reportedHashrate }, data.data.map { it.currentHashrate }, data.data.map { it.averageHashrate }, enum)
                }
            }

            dataset.add(reported)
            dataset.add(current)
            dataset.add(average)

            chart.applyStyle(times, dataset)
            chart.axisLeft.valueFormatter = MyFormatter(getStringExtra("isChia") == Crypto.XCH.name)
        }
    }

    private fun woolyPoolyGroup(
        enum: Time,
        isSolo: Boolean = false
    ) {
        val data = getExtraAs(WoolyPoolyResponse::class.java)

        empty()

        val list = if(isSolo) data.perfomance.solo else data.perfomance.pplns

        init(emptyList(), emptyList(), list.map{ it.hashrate }.reversed(), emptyList(), enum)
    }

    class MyFormatter(private val isChia: Boolean) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return value.formatReadable(!isChia)
        }
    }

    private fun init(timeList: List<Long>, reportedList: List<Float?>, currentList: List<Float?>, averageList: List<Float?>, enum: Time) {
        safeContext {
            reported = lineDataSetFilledDrawableWithReadable(getString(R.string.reported), reportedList.takeLast(enum.value), R.color.green, R.drawable.bg_fade_green)
            current = lineDataSetFilledDrawableWithReadable(getString(R.string.current), currentList.takeLast(enum.value), R.color.primaryBlue, R.drawable.bg_fade_blue)
            average = lineDataSetFilledDrawableWithReadable(getString(R.string.average), averageList.takeLast(enum.value), R.color.yellow, R.drawable.bg_fade_yellow)
            times.clear()
            times.addAll(timeList.map { it.toDateEpoch(Constant.HOUR_MINUTE, Locale.ENGLISH) }.takeLast(enum.value))
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
}