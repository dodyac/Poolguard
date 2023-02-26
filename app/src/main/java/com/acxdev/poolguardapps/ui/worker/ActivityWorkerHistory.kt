package com.acxdev.poolguardapps.ui.worker

import com.acxdev.commonFunction.util.ext.toDate
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.set
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityWorkerHistoryBinding
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.rest.EzilType
import com.acxdev.poolguardapps.rest.fetchApiBitFly
import com.acxdev.poolguardapps.rest.fetchApiEzil
import com.acxdev.poolguardapps.rest.fetchApiFlexPool
import com.acxdev.poolguardapps.rest.fetchApiNanoPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.dashboard.RowSlide
import com.acxdev.poolguardapps.util.fixHashRate
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.initChartHashShares
import com.acxdev.poolguardapps.util.invalidateFormat
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZGetById
import java.text.DecimalFormat
import java.util.*

class ActivityWorkerHistory : BaseActivity<ActivityWorkerHistoryBinding>(ActivityWorkerHistoryBinding::inflate) {

    private lateinit var wallet: Wallet

    companion object {
        const val WALLET_ID = "wallet_id"
        const val WORKER_NAME = "worker_name"
    }

    override fun ActivityWorkerHistoryBinding.configureViews() {
        wallet = sqLiteZGetById(Wallet::class.java, intent.getStringExtra(WALLET_ID)!!.toLong())
        val worker = intent.getStringExtra(WORKER_NAME)

        toolbar.set(worker!!)
        when(wallet.pool) {
            Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                sliderShares.root.gone()

                val currentDate = System.currentTimeMillis()
                val daysBefore = currentDate - (24 * 60 * 60 * 1000)
                //TODO Raw String
                val ezilDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

                fetchApiEzil(EzilType.STATS).chartWorker(wallet.address, worker, daysBefore.toDate(ezilDateFormat, Locale.ENGLISH),
                    currentDate.toDate(ezilDateFormat, Locale.ENGLISH)).onLoaded(this@ActivityWorkerHistory) {
                    initChart(this)
                }
                fetchApiEzil(EzilType.STATS).currentStatsWorker(wallet.address, worker).onLoaded(this@ActivityWorkerHistory) {
                    val reported = if(wallet.symbol == Crypto.ETHW.name) eth.reported_hashrate else etc.reported_hashrate
                    val current = if(wallet.symbol == Crypto.ETHW.name) eth.current_hashrate else etc.current_hashrate
                    val average = if(wallet.symbol == Crypto.ETHW.name) eth.average_hashrate else etc.average_hashrate
                    initSliderHash(reported, current, average)
                }
            }
            Pool.NANOPOOL.value -> {
                sliderShares.root.gone()
                fetchApiNanoPool(wallet.getBaseUrl()).currentHashRateWorker(wallet.address.removeCfx(), worker).onLoaded(this@ActivityWorkerHistory) {
                    val current = data.fixHashRate(wallet.symbol)
                    fetchApiNanoPool(wallet.getBaseUrl()).averageHashRateWorker(wallet.address.removeCfx(), worker).onLoaded(this@ActivityWorkerHistory) {
                        val average = data.h24.fixHashRate(wallet.symbol)
                        fetchApiNanoPool(wallet.getBaseUrl()).reportedHashRateWorker(wallet.address.removeCfx(), worker).onLoaded(this@ActivityWorkerHistory) {
                            initSliderHash(data.fixHashRate(wallet.symbol), current, average)
                        }
                    }
                }
                fetchApiNanoPool(wallet.getBaseUrl()).reportedHashRateSharesChartWorker(wallet.address.removeCfx(), worker).onLoaded(this@ActivityWorkerHistory) {
                    val report = data
                    data.forEachIndexed { index, nanopoolHashRateChartData ->
                        report[index].hashrate = nanopoolHashRateChartData.current.fixHashRate(wallet.symbol)
                    }
                    fetchApiNanoPool(wallet.getBaseUrl()).currentHashRateChartWorker(wallet.address.removeCfx(), worker).onLoaded(this@ActivityWorkerHistory){
                        data.forEachIndexed { index, nanopoolHashRateChartData ->
                            report[index].current = nanopoolHashRateChartData.hashrate.fixHashRate(wallet.symbol)
                        }
                        initChart(report)
                    }
                }
            }
            Pool.FLEXPOOL.value -> {
                fetchApiFlexPool().workerStats(wallet.symbol, wallet.address, worker).onLoaded(this@ActivityWorkerHistory) {
                    initSliderHash(result.reportedHashrate, result.currentEffectiveHashrate, result.averageEffectiveHashrate)
                    initSliderShares(result.validShares, result.staleShares, result.invalidShares)
                }
                fetchApiFlexPool().workerChart(wallet.symbol, wallet.address, worker).onLoaded(this@ActivityWorkerHistory) { initChart(this) }
            }
            Pool.ETHERMINE.value, Pool.FLYPOOL.value -> {
                fetchApiBitFly(wallet.getBaseUrl()).workerCurrentStats(wallet.address, worker).onLoaded(this@ActivityWorkerHistory) {
                    initSliderHash(data?.reportedHashrate, data?.currentHashrate, data?.averageHashrate)
                    initSliderShares(data?.validShares, data?.staleShares, data?.invalidShares)
                }
                fetchApiBitFly(wallet.getBaseUrl()).workerHistory(wallet.address, worker).onLoaded(this@ActivityWorkerHistory) { initChart(data) }
            }
        }
    }

    override fun ActivityWorkerHistoryBinding.onClickListener() {

    }

    private fun initSliderHash(reported: Float?, current: Float?, average: Float?) {
        scopeLayout {
            val hashRate = mutableListOf<Menu>()
            if(wallet.pool != Pool.HEROMINERS.value)
                if(wallet.symbol == Crypto.ETHW.name || wallet.symbol == Crypto.ETC.name) hashRate.add(
                    Menu(getString(R.string.reported), reported.invalidateFormat(wallet.symbol))
                )
            hashRate.add(Menu(getString(R.string.current), current.invalidateFormat(wallet.symbol)))
            hashRate.add(Menu(getString(R.string.average), average.invalidateFormat(wallet.symbol)))
            sliderHashRate.sliderView.set(RowSlide(hashRate))
        }
    }

    private fun initSliderShares(valid: Float?, stale: Float?, invalid: Float?) {
        scopeLayout {
            val shares = mutableListOf<Menu>()
            shares.add(Menu(getString(R.string.valid), DecimalFormat("0.##").format(valid ?: 0)))
            shares.add(Menu(getString(R.string.stale), DecimalFormat("0.##").format(stale ?: 0)))
            shares.add(Menu(getString(R.string.invalid), DecimalFormat("0.##").format(invalid ?: 0)))
            sliderShares.sliderView.set(RowSlide(shares))
        }
    }

    private fun <T> initChart(data: T) {
        scopeLayout {
            initChartHashShares(chartShimmer, chart, wallet, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scopeLayout {
            sliderHashRate.sliderView.stopAutoCycle()
            sliderShares.sliderView.stopAutoCycle()
        }
    }
}