package com.acxdev.poolguardapps.ui.dashboard

import android.util.TypedValue
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.fetchApiOtherPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchBalance
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchChart
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchEstimated
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchPayments
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchSlider
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchWorkers
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initBalance
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initBalanceCrypto
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initChart
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initEstimated
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initInfo
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initPayments
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initSliderHash
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initSliderShares
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.initWorkers
import com.acxdev.poolguardapps.util.CryptoFormat
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.initRedGreenIndicator
import com.acxdev.poolguardapps.util.invalidateBaikalMine
import com.acxdev.poolguardapps.util.invalidateK1Pool
import com.acxdev.poolguardapps.util.invalidateMyMiners
import com.acxdev.poolguardapps.util.invalidateSoloPool
import com.acxdev.poolguardapps.util.pool
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toLocalCurrency
import com.acxdev.poolguardapps.util.whatToMine

object DashboardFunc2 {
    fun ActivityDashboard.initUI() {
        scopeDashboard {
            unpaid.crypto.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(com.acxdev.commonFunction.R.dimen.text_header)
            )

            unpaid.text.text = getString(R.string.unpaidBalance)
            immature.text.text = getString(R.string.immatureBalance)
            coin.imageUrl(wallet.symbol.crypto().icon)
            coinImage.imageUrl(wallet.symbol.crypto().icon)
            coinSymbol.text = wallet.symbol
            coinName.text = wallet.symbol.crypto().fullName
            estimated.crypto.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(com.acxdev.commonFunction.R.dimen.text_header)
            )

            netHash.text.text = "Nethash"
            diff.text.text = getString(R.string.difficulty)
            block.text.text = getString(R.string.latestBlocks)
            poolIcon.imageUrl(wallet.pool().icon)
            poolName.text = wallet.pool().value
        }
    }

    fun ActivityDashboard.initObserve() {
        scopeDashboard {
            fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getPriceMultiFull(wallet.symbol, currency).onLoaded(this@initObserve) {
                coinExchange.text = RAW[wallet.symbol]?.get(currency)?.PRICE?.toLocalCurrency(this@initObserve)
                val percentage = RAW[wallet.symbol]?.get(currency)?.CHANGEPCT24HOUR ?: 0
                initRedGreenIndicator(coinPTC, arrow, percentage,percentage.toFloat() < 0F)
            }
        }
    }

    fun String?.initializeAlertFailure() {
//        when(isDigitsOnly()) {
//            is Int -> {
//                root.snackBar(
//                    Toast.ERROR,
//                    getString(
//                        R.string.stripes,
//                        toString(),
//                        getMessage()
//                    )
//                )
//            }
//            else -> {
//                root.snackBar(
//                    Toast.ERROR,
//                    this ?: "An error occured"
//                )
//            }
//        }
    }

    fun Double.invalidateAllBalance(wallet: Wallet): Double {
        return try {
            when(wallet.pool) {
                Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> invalidateMyMiners(wallet.symbol)
                Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> invalidateBaikalMine(wallet.symbol)
                Pool.SOLOPOOL.value -> invalidateSoloPool(wallet.symbol)
                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                    if(wallet.symbol == Crypto.CTXC.name || wallet.symbol == Crypto.ETC.name || wallet.symbol == Crypto.ETHW.name)
                        this * 1000000000.0 else this
                }
                Pool.CRAZYPOOL.value -> {
                    if(wallet.symbol != Crypto.UBQ.name) this * 1000000000.0 else this
                }
                Pool.ZETPOOLPPS.value, Pool.COMINERS.value, Pool.ZETPOOL.value -> this * 1000000000.0
                else -> this
            }
        } catch (e: Exception) {
            0.0
        }
    }

    fun ActivityDashboard.k1poolGroupExceptRbpps(isRbpps: Boolean = false) {
        scopeDashboard {
            immature.root.visible()
            if(isRbpps) immature.text.text = getString(R.string.expectedBalance)
            sliderShares.root.gone()
            fetchApiOtherPool(wallet.getBaseUrl()).k1Pool(wallet.address).onLoaded(this@k1poolGroupExceptRbpps) {
                try {
                    initSliderHash(miner.curHashrate, miner.curHashrate, miner.avgHashrate)

                    val invalidateImmature = if(isRbpps) miner.expectedBalance else miner.immatureBalance

                    initBalanceCrypto(miner.pendingBalance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), invalidateImmature.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL))

                    whatToMine(wallet.symbol, miner.avgHashrate){
                        initBalance(price, miner.pendingBalance.toBigDecimal(), invalidateImmature.toBigDecimal())
                        initEstimated(this)
                        initInfo(whattomine)
                    }
                    initChart(this)

                    val payout = mutableListOf<PaymentList>()
                    miner.payments.forEach {
                        payout.add(
                            PaymentList(it.amount.invalidateK1Pool(wallet.symbol)
                                .toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(), it.tx, wallet.symbol, it.timestamp)
                        )
                    }
                    initPayments(payout)

                    try {
                        val workerList = mutableListOf<WorkerList>()
                        miner.workers.toList().forEach {
                            workerList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                        }
                        initWorkers(workerList)
                    } catch (e: Exception){ catchWorkers() }
                } catch (e: Exception){
                    catchSlider()
                    catchBalance()
                    catchEstimated()
                    catchChart()
                    catchPayments()
                    catchWorkers()
                }
            }
        }
    }

    fun ActivityDashboard.minerStatsMain() {
        fetchApiOtherPool().minerStatsMain("${wallet.getBaseUrl()}accounts/${wallet.address}").onLoaded(this) {
            val invalidateBalance = stats.balance.invalidateAllBalance(wallet)
            val invalidateImmature = stats.immature.invalidateAllBalance(wallet)

            initBalanceCrypto(invalidateBalance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL),
                invalidateImmature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL))

            whatToMine(wallet.symbol, hashrate){
                initBalance(price, invalidateBalance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal(),
                    invalidateImmature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal())
                initEstimated(this)
                initInfo(whattomine)
            }

            val reportedHashRateIfAvailable = reportedHashrate ?: currentHashrate
            initSliderHash(reportedHashRateIfAvailable, currentHashrate, hashrate)

            initChart(this)

            when(wallet.pool) {
                Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> {
                    initSliderShares(A, S, I)
                }
                Pool.CRAZYPOOL.value -> {
                    initSliderShares(stats.validCurrent, stats.stalesCurrent, stats.invalidCurrent)
                }
                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                    //Sometimes 2Miners indicate to be null
                    if(minerCharts == null) {
                        fetchApiOtherPool(wallet.getBaseUrl()).twoMinersChart(wallet.address).onLoaded(this@minerStatsMain) {
                            initChart(this)
                        }
                    }
                }
            }

            try {
                val payout = mutableListOf<PaymentList>()

                payments.forEach { payment ->
                    val invalidateAmount = payment.amount.invalidateAllBalance(wallet)
                    payout.add(PaymentList(invalidateAmount, payment.tx, wallet.symbol, payment.timestamp))
                }

                initPayments(payout)
            } catch (e: Exception) {
                catchPayments()
            }

            try {
                val workerList = mutableListOf<WorkerList>()

                workers.toList().forEach { worker ->
                    when(wallet.pool) {
                        Pool.ZETPOOLPPS.value,
                        Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value,
                        Pool.SOLOPOOL.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value,
                        Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                            workerList.add(WorkerList(worker.first, worker.second.hr, worker.second.hr2))
                        }
                        Pool.COMINERS.value -> {
                            workerList.add(
                                WorkerList(worker.first, worker.second.hr, worker.second.hr2,
                                    valid = worker.second.v_per, stale = worker.second.i_per)
                            )
                        }
                        Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> {
                            workerList.add(WorkerList(worker.first, worker.second.hr, worker.second.hr2, worker.second.A, worker.second.S))
                        }
                    }
                }
                initWorkers(workerList)
            } catch (e: Exception) {
                catchWorkers()
            }
        }
    }

    fun ActivityDashboard.minerStatsWoolyPooly() {
        fetchApiOtherPool(wallet.getBaseUrl()).woolyPooly(wallet.address).onLoaded(this@minerStatsWoolyPooly) {
            try {
                val isSolo = wallet.pool == Pool.WOOLYPOOLYSOLO.value

                val currentHash = if(isSolo) mode_stats.solo.default.currentHashrate else mode_stats.pplns.default.currentHashrate
                val averageHash = if(isSolo) mode_stats.solo.default.dayHashrate else mode_stats.pplns.default.dayHashrate

                initSliderHash(currentHash, currentHash, averageHash)

                initBalanceCrypto(stats.balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL),
                    stats.immature_balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL))

                whatToMine(wallet.symbol, averageHash){
                    initBalance(price, stats.balance.toBigDecimal(), stats.immature_balance.toBigDecimal())
                    initEstimated(this)
                    initInfo(whattomine)
                }

                initChart(this)

                val payout = mutableListOf<PaymentList>()
                payments.forEach { payment ->
                    payout.add(
                        PaymentList(payment.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                            payment.tx, wallet.symbol, payment.timestamp)
                    )
                }

                initPayments(payout)

                val workerList = mutableListOf<WorkerList>()
                workers.forEach { worker ->
                    workerList.add(WorkerList(worker.worker, worker.hr, worker.hr3))
                }

                initWorkers(workerList)
            } catch (e: Exception){
                catchSlider()
                catchBalance()
                catchEstimated()
                catchChart()
                catchPayments()
                catchWorkers()
            }
        }
    }
}