package com.acxdev.poolguardapps.ui.dashboard

import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.toDate
import com.acxdev.commonFunction.util.ext.toPercent
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.set
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.commonFunction.util.toasty
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.rest.EzilType
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.fetchApiBitFly
import com.acxdev.poolguardapps.rest.fetchApiEzil
import com.acxdev.poolguardapps.rest.fetchApiFlexPool
import com.acxdev.poolguardapps.rest.fetchApiHiveOn
import com.acxdev.poolguardapps.rest.fetchApiNanoPool
import com.acxdev.poolguardapps.rest.fetchApiOtherPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc2.k1poolGroupExceptRbpps
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc2.minerStatsMain
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc2.minerStatsWoolyPooly
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
import com.acxdev.poolguardapps.util.calculateLocalCurrency
import com.acxdev.poolguardapps.util.chiaFormula
import com.acxdev.poolguardapps.util.fixHashRate
import com.acxdev.poolguardapps.util.fixHashRateUnMineable
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.removePrefixAddress
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toEpochHiveOn
import com.acxdev.poolguardapps.util.toEpochHiveOnChart
import com.acxdev.poolguardapps.util.whatToMine
import java.math.BigDecimal
import java.util.*

object DashboardFunc {
    fun ActivityDashboard.initMain() {
        scopeDashboard {
            looper = 0
            when(wallet.pool) {
                Pool.FLOCKPOOL.value -> {
                    immature.root.visible()
                    fetchApiOtherPool(wallet.getBaseUrl()).flockPoolDashboard(wallet.address).onLoaded(this@initMain) {
                        try {
                            val currentHash = workers.map { it.hashrate.now }.sum()
                            val avgHash = workers.map { it.hashrate.avg }.sum()
                            initSliderHash(currentHash, currentHash, avgHash)

                            val valid = workers.sumOf { it.shares.accepted }.toFloat()
                            val stale = workers.sumOf { it.shares.stale }.toFloat()
                            val invalid = workers.sumOf { it.shares.rejected }.toFloat()
                            initSliderShares(valid, stale, invalid)

                            val balances = balance.mature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL)
                            val immatureBalance = balance.immature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL)
                            initBalanceCrypto(balances, immatureBalance)
                            initChart(this)

                            whatToMine(wallet.symbol, avgHash) {
                                initBalance(price, balance.mature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal(),
                                    balance.immature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal())

                                initEstimated(this)
                                initInfo(whattomine)
                            }

                            val workerList = mutableListOf<WorkerList>()
                            workers.forEach {
                                workerList.add(
                                    WorkerList(it.name, it.hashrate.now, it.hashrate.avg,
                                        it.shares.accepted.toFloat(), it.shares.stale.toFloat())
                                )
                            }
                            initWorkers(workerList)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchBalance()
                            catchChart()
                            catchEstimated()
                            catchSlider()
                            catchWorkers()
                        }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).flockPoolPayments(wallet.address).onLoaded(this@initMain) {
                        try {
                            val paymentList = mutableListOf<PaymentList>()
                            payments.sortedByDescending { it.timestamp }.forEach {
                                paymentList.add(PaymentList(it.amount, it.transaction_id, wallet.symbol, it.timestamp))
                            }
                            initPayments(paymentList)
                        } catch (e: Exception) {
                            catchPayments()
                        }
                    }
                }
                Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> {
                    //HIDE PAYMENT INFO
                    payout.hideShimmer()
                    payoutLayout.gone()

                    immature.root.visible()
                    sliderShares.root.gone()
                    fetchApiOtherPool(wallet.getBaseUrl()).minerPool(wallet.address, 1).onLoaded(this@initMain) {
                        try {
                            initSliderHash(hashAvg5m, hashAvg5m, hashAvg24h)
                            initBalanceCrypto(balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), immature.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL))
                            initChart(this)

                            whatToMine(wallet.symbol, hashAvg24h) {
                                initBalance(price, balance.toBigDecimal(), immature.toBigDecimal())

                                initEstimated(this)
                                initInfo(whattomine)
                            }

                            val workerList = mutableListOf<WorkerList>()
                            workers.toList().forEach {
                                workerList.add(
                                    WorkerList(it.first.removePrefixAddress(wallet), it.second.hashrate.toFloat(), it.second.hashrate.toFloat(),
                                        it.second.shares.toFloat(), it.second.invalidshares.toFloat())
                                )
                            }
                            initWorkers(workerList)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                Pool.UNMINEABLE.value -> {
                    sliderShares.root.gone()
                    fetchApiOtherPool(wallet.getBaseUrl()).unMineableStats(wallet.address, wallet.symbol).onLoaded(this@initMain) {
                        try {
                            initBalanceCrypto(data.balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), emptyString())

                            val balance = data.balance
                            fetchApiOtherPool(wallet.getBaseUrl()).unMineableWorker(data.uuid).onLoaded(this@initMain) {
                                try {

                                    initChart(data)

                                    val etHash = data.ethash.workers
                                    val etcHash = data.etchash.workers
                                    val randomX = data.randomx.workers
                                    val kawpow = data.kawpow.workers

                                    val workerList = mutableListOf<WorkerList>()
                                    etHash.forEach {
                                        workerList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                                    }
                                    etcHash.forEach {
                                        workerList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                                    }
                                    randomX.forEach {
                                        workerList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                                    }
                                    kawpow.forEach {
                                        workerList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                                    }
                                    initWorkers(workerList)

                                    val totalHashCur = etHash.sumOf { it.chr.toDouble() } + etcHash.sumOf { it.chr.toDouble() } + randomX.sumOf { it.chr.toDouble() } + kawpow.sumOf { it.chr.toDouble() }
                                    val totalHashRep = etHash.sumOf { it.rhr.toDouble() } + etcHash.sumOf { it.rhr.toDouble() } + randomX.sumOf { it.rhr.toDouble() } + kawpow.sumOf { it.rhr.toDouble() }

                                    whatToMine(wallet.symbol, totalHashCur.fixHashRateUnMineable(wallet.symbol)){
                                        initBalance(price, balance.toBigDecimal(), 0.toBigDecimal())
                                        initEstimated(this)
                                        initInfo(whattomine)
                                    }
                                    initSliderHash(
                                        totalHashRep.fixHashRateUnMineable(wallet.symbol),
                                        totalHashCur.fixHashRateUnMineable(wallet.symbol),
                                        totalHashRep.fixHashRateUnMineable(wallet.symbol)
                                    )
                                } catch (e: Exception) { e.printStackTrace() }
                            }
                            fetchApiOtherPool(wallet.getBaseUrl()).unMineablePayments(data.uuid).onLoaded(this@initMain) {
                                try {
                                    val payout = mutableListOf<PaymentList>()

                                    data.list.forEach {
                                        payout.add(PaymentList(it.amount, it.tx, data.coin, it.timestamp / 1000))
                                    }
                                    initPayments(payout)
                                } catch (e: Exception) {
                                    catchPayments()
                                }
                            }
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                }
                Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                    sliderShares.root.gone()
                    immature.root.visibleIf(wallet.pool == Pool.EZIL_ZIL.value)
                    immature.text.text = Crypto.ZIL.name

                    val currentDate = System.currentTimeMillis()
                    val daysBefore = currentDate - (24 * 60 * 60 * 1000)
                    //TODO Raw String
                    val ezilDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    fetchApiEzil(EzilType.STATS).chart(wallet.address, daysBefore.toDate(ezilDateFormat, Locale.ENGLISH),
                        currentDate.toDate(ezilDateFormat, Locale.ENGLISH)).onLoaded(this@initMain) {
                        try {
                            initChart(this)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchChart()
                        }
                    }
                    fetchApiEzil(EzilType.STATS).worker(wallet.address).onLoaded(this@initMain) {
                        try {
                            val workerList = mutableListOf<WorkerList>()
                            forEach {
                                workerList.add(WorkerList(it.worker, it.current_hashrate, it.average_hashrate))
                            }
                            initWorkers(workerList)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchWorkers()
                        }
                    }
                    fetchApiEzil(EzilType.STATS).currentStats(wallet.address).onLoaded(this@initMain) {
                        try {
                            val reported = if(wallet.symbol == Crypto.ETHW.name) eth.reported_hashrate else etc.reported_hashrate
                            val current = if(wallet.symbol == Crypto.ETHW.name) eth.current_hashrate else etc.current_hashrate
                            val average = if(wallet.symbol == Crypto.ETHW.name) eth.average_hashrate else etc.average_hashrate
                            initSliderHash(reported, current, average)

                            fetchApiEzil(EzilType.BILLING).balance(wallet.address).onLoaded(this@initMain) {
                                try {
                                    initBalanceCrypto(eth.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), zil.toCrypto(Crypto.ZIL.name, CryptoFormat.FORMAT_SYMBOL))

                                    val zilBalance = zil

                                    whatToMine(wallet.symbol, average){
                                        initBalance(price, eth.toBigDecimal(), null)
                                        immature.currency.showShimmer()
                                        fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(Crypto.ZIL.name, currency).onLoaded(this@initMain) {
                                            try {
                                                immature.currency.text = zilBalance.calculateLocalCurrency(this[currency]!!, this@initMain)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                e.localizedMessage?.let { toasty(Toast.ERROR, it) }
                                            }
                                        }

                                        initEstimated(this)
                                        initInfo(whattomine)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    catchBalance()
                                    catchEstimated()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchSlider()
                            catchBalance()
                            catchEstimated()
                        }
                    }
                    fetchApiEzil(EzilType.BILLING).payout(wallet.address).onLoaded(this@initMain) {
                        try {
                            val payout = mutableListOf<PaymentList>()

                            eth.forEach {
                                payout.add(
                                    PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                                        it.tx_hash, Crypto.ETHW.name, it.created_at.toEpochHiveOnChart())
                                )
                            }

                            etc.forEach {
                                payout.add(
                                    PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                                        it.tx_hash, Crypto.ETC.name, it.created_at.toEpochHiveOnChart())
                                )
                            }

                            zil.forEach {
                                payout.add(
                                    PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                                        it.tx_hash, Crypto.ZIL.name, it.created_at.toEpochHiveOnChart())
                                )
                            }

                            initPayments(payout)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchPayments()
                        }
                    }
                }
                Pool.HIVEON.value -> {
                    fetchApiHiveOn().overview(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                        try {
                            initSliderHash(reportedHashrate, hashrate, hashrate24h)
                            initSliderShares(sharesStatusStats.validCount, sharesStatusStats.staleCount, 0F)
                            fetchApiHiveOn().billing(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                                try {
                                    initBalanceCrypto(totalUnpaid.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), "")

                                    whatToMine(wallet.symbol, hashrate24h) {
                                        initBalance(price, totalUnpaid.toBigDecimal(), 0.toBigDecimal())
                                        initEstimated(this)
                                        initInfo(whattomine)
                                    }
                                } catch (e: Exception) {
                                    catchBalance()
                                    catchEstimated()
                                }
                            }
                        } catch (e: Exception){
                            catchSlider()
                            catchBalance()
                            catchEstimated()
                        }
                    }
                    fetchApiHiveOn().payouts(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                        try {
                            val payout = mutableListOf<PaymentList>()
                            items.forEach {
                                payout.add(
                                    PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                                        it.txHash, wallet.symbol, it.createdAt.toEpochHiveOn())
                                )
                            }
                            initPayments(payout)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchPayments()
                        }
                    }
                    fetchApiHiveOn().hashrateChart(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                        try {
                            val chart = this
                            fetchApiHiveOn().sharesChart(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                                try {
                                    chart.shares = this
                                    initChart(chart)
                                } catch (e: Exception) {
                                    catchChart()
                                }
                            }
                        } catch (e: Exception) {
                            catchChart()
                        }
                    }
                    fetchApiHiveOn().workers(wallet.address.remove0x(), wallet.symbol).onLoaded(this@initMain) {
                        try {
                            val workerList = mutableListOf<WorkerList>()
                            workers.toList().forEach {
                                workerList.add(
                                    WorkerList(it.first, it.second.hashrate, it.second.hashrate24h,
                                        it.second.sharesStatusStats.validCount, it.second.sharesStatusStats.staleCount)
                                )
                            }
                            initWorkers(workerList)
                        } catch (e: Exception){ catchWorkers() }
                    }
                }
                Pool.RAVENMINER.value -> {
                    sliderShares.root.gone()
                    fetchApiOtherPool(wallet.getBaseUrl()).ravenMiner(wallet.address).onLoaded(this@initMain) {
                        try {
                            initSliderHash(hashrate.the5Min, hashrate.the1H, hashrate.the24H)

                            initBalanceCrypto(balance.pending.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), balance.pending.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL))

                            whatToMine(wallet.symbol, hashrate.the24H){
                                initBalance(price, balance.pending.toBigDecimal(), balance.pending.toBigDecimal())
                                initEstimated(this)
                                initInfo(whattomine)
                            }
                            initChart(this)

                            val payout = mutableListOf<PaymentList>()
                            payouts.forEach {
                                payout.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(), it.tx, wallet.symbol, it.time))
                            }
                            initPayments(payout)

                            try {
                                val workerList = mutableListOf<WorkerList>()
                                workers.list.forEach {
                                    workerList.add(WorkerList(it.name, it.hr5m, it.hr5m))
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
                Pool.COMINERS.value -> {
                    minerStatsMain()
                    immature.root.visible()
                    sliderShares.root.gone()
                }
                Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> {
                    minerStatsMain()
                    sliderShares.root.gone()
                }
                Pool.MINEXMR.value -> {
                    sliderShares.root.gone()
                    fetchApiOtherPool(wallet.getBaseUrl()).mineXmrBalance(wallet.address).onLoaded(this@initMain) {
                        try {
                            initBalanceCrypto(balance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL), emptyString())
                            fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(wallet.symbol, currency).onLoaded(this@initMain) {
                                initBalance(this[currency]!!, balance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal(), ConstantLib.ZERO.toBigDecimal())
                            }
                        } catch (e: Exception) { catchBalance() }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).mineXmrWorker(wallet.address).onLoaded(this@initMain) {
                        try {
                            val workerList = mutableListOf<WorkerList>()
                            forEach {
                                workerList.add(WorkerList(it.name, it.hashrate, it.hashrate))
                            }
                            initWorkers(workerList)

                            val hashrate = sumOf { it.hashrate.toDouble() }.toFloat()
                            whatToMine(wallet.symbol, hashrate){
                                initEstimated(this)
                                initInfo(whattomine)
                            }

                            initSliderHash(hashrate, hashrate, hashrate)
                        } catch (e: Exception){
                            catchSlider()
                            catchWorkers()
                            catchEstimated()
                        }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).mineXmrChart(1, wallet.address).onLoaded(this@initMain) {
                        try {
                            initChart(this)
                        } catch (e: Exception){ initChart(null) }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).mineXmrPayments(wallet.address).onLoaded(this@initMain) {
                        try {
                            val paymentList = mutableListOf<PaymentList>()
                            payments.forEach { payments ->
                                val split = payments.split(Constant.COLON)
                                val date = split[0].toLong()
                                val txHash = split[1]
                                val amount = split[2].toDoubleOrNull()
                                val fee = split[3]
                                val idk = split[4]
                                amount?.let { PaymentList(it, txHash, wallet.symbol, date) }?.let { paymentList.add(it) }
                            }
                            initPayments(paymentList)
                        } catch (e: Exception){ catchPayments() }
                    }
                }
                Pool.ETHO.value -> {
                    minerStatsMain()
                    immature.root.visible()
                    sliderShares.root.gone()
                }
                Pool.SOLOPOOL.value -> {
                    minerStatsMain()
                    sliderShares.root.gone()
                    immature.root.visible()
                }
                Pool.K1POOLRBPPS.value -> k1poolGroupExceptRbpps(true)
                Pool.K1POOL.value, Pool.K1POOLSOLO.value -> k1poolGroupExceptRbpps()
                Pool.LUCKPOOL.value -> {
                    immature.root.visible()
                    fetchApiOtherPool(wallet.getBaseUrl()).luckPool(wallet.address).onLoaded(this@initMain) {
                        try {
                            val invalidateBalance = if(wallet.symbol == Crypto.ZEC.name) balance.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else balance
                            val invalidateImmature = if(wallet.symbol == Crypto.ZEC.name) immature.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else immature
                            initBalanceCrypto(invalidateBalance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL), invalidateImmature.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL))
                            whatToMine(wallet.symbol, hashrateSols){
                                initBalance(price, invalidateBalance.toBigDecimal(), invalidateImmature.toBigDecimal())
                                initEstimated(this)
                                initInfo(whattomine)
                            }

                            initSliderHash(0F, hashrateSols, avgHashrateSols24HR)

                            val shares = mutableListOf<Menu>()
                            shares.add(Menu(getString(R.string.shares), String.format("%.0f", shares)))
                            shares.add(Menu(getString(R.string.efficiency), efficiency.toPercent()))
                            shares.add(Menu(getString(R.string.luck), estimatedLuck))
                            sliderShares.sliderView.set(RowSlide(shares))

                            try {
                                val workerList = mutableListOf<WorkerList>()
                                workers.forEach {
                                    val split = it.split(Constant.COLON)
                                    val name = split[0]
                                    val hashRate = split[1].toFloatOrNull()
                                    val share = split[2].toFloatOrNull()
                                    val isOn = split[3]
                                    val tag = split[4]
                                    val isFalse = split[5]

                                    workerList.add(WorkerList(name, hashRate, hashRate))
                                }
                                initWorkers(workerList)
                            } catch (e: Exception){ catchWorkers() }
                        } catch (e: Exception){
                            catchSlider()
                            catchBalance()
                            catchEstimated()
                            catchWorkers()
                        }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).luckPoolPayments(wallet.address).onLoaded(this@initMain) {
                        try {
                            val paymentList = mutableListOf<PaymentList>()
                            forEach {
                                val split = it.split(Constant.COLON)
                                val time = split[0].toLong()
                                val txHash = split[1]
                                val amount = split[2].toDouble()
                                val invalidateAmount = if(wallet.symbol == Crypto.ZEC.name) amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else amount

                                paymentList.add(PaymentList(invalidateAmount, txHash, wallet.symbol, time))
                            }
                            initPayments(paymentList)
                        } catch (e: Exception){ catchPayments() }
                    }
                    fetchApiOtherPool(wallet.getBaseUrl()).luckPoolChart(wallet.address).onLoaded(this@initMain) {
                        initChart(this)
                    }
                }
                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                    minerStatsMain()
                    immature.root.visible()
                    sliderShares.root.gone()
                }
                Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> {
                    minerStatsMain()
                    sliderShares.root.gone()
                }
                Pool.CRAZYPOOL.value -> {
                    minerStatsMain()
                    immature.root.visibleIf(wallet.symbol != Crypto.ETHW.name)
                }
                Pool.WOOLYPOOLY.value, Pool.WOOLYPOOLYSOLO.value -> {
                    immature.root.visible()
                    sliderShares.root.gone()
                    minerStatsWoolyPooly()
                }
                Pool.HEROMINERS.value -> {
                    immature.root.visibleIf(wallet.symbol != Crypto.RVN.name)
                    fetchApiOtherPool(wallet.getBaseUrl()).heroMiners(wallet.address).onLoaded(this@initMain) {
                        try {
                            initSliderHash(0F, stats.hashrate, stats.hashrate_24h)
                            initSliderShares(stats.shares_good, stats.shares_stale, stats.shares_invalid)
                            initBalanceCrypto(stats.balance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL),
                                unconfirmed?.sumOf { unconfirmed -> unconfirmed.reward.toDouble() }.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL))
                            initChart(this)

                            whatToMine(wallet.symbol, stats.hashrate_24h) {
                                initBalance(price, stats.balance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal(),
                                    unconfirmed?.sumOf { unconfirmed -> unconfirmed.reward.toDouble() }.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal())
                                initEstimated(this)
                                initInfo(whattomine)
                            }

                            val payout = mutableListOf<PaymentList>()
                            val hash = mutableListOf<String>()
                            val date = mutableListOf<Long>()
                            payments?.forEachIndexed { index, s ->
                                if(index % 2 != 0) date.add(s.toLong())
                                else hash.add(s)
                            }
                            hash.forEachIndexed { index, s ->
                                val split = s.split(Constant.COLON)
                                val txHash = split[0]
                                val amount = split[1].toDouble()
                                val fee = split[2]
                                payout.add(PaymentList(amount, txHash, wallet.symbol, date[index]))
                            }

                            initPayments(payout)

                            val workersList = mutableListOf<WorkerList>()
                            workers.forEach { worker ->
                                if(worker.lastShare > 0L) {
                                    workersList.add(WorkerList(worker.name, worker.hashrate, worker.hashrate_24h, worker.shares_good, worker.shares_stale))
                                }
                            }
                            initWorkers(workersList)
                        } catch (e: Exception){
                            catchSlider()
                            catchBalance()
                            catchEstimated()
                            catchPayments()
                            catchWorkers()
                            initChart(null)
                        }
                    }
                }
                Pool.NANOPOOL.value -> {
                    sliderShares.root.gone()
                    immature.root.visibleIf(wallet.symbol != Crypto.ETHW.name && wallet.symbol != Crypto.ZEC.name && wallet.symbol != Crypto.XMR.name)

                    fetchApiNanoPool(wallet.getBaseUrl()).stats(wallet.address.removeCfx()).onLoaded(this@initMain) {
                        try {
                            val dataGeneral = data
                            fetchApiNanoPool(wallet.getBaseUrl()).reportedHashRate(wallet.address.removeCfx()).onLoaded(this@initMain) {
                                initSliderHash(data.fixHashRate(wallet.symbol), dataGeneral?.hashrate.fixHashRate(wallet.symbol), dataGeneral?.avgHashrate?.h24.fixHashRate(wallet.symbol))
                            }

                            initBalanceCrypto(data?.balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL),
                                data?.unconfirmed_balance.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL))

                            var unpaid: BigDecimal
                            var immature: BigDecimal
                            try {
                                unpaid = data!!.balance.toBigDecimal()
                                immature = data.unconfirmed_balance.toBigDecimal()
                            } catch (e: Exception){
                                unpaid = 0.toBigDecimal()
                                immature = 0.toBigDecimal()
                            }

                            try {
                                val workerList = mutableListOf<WorkerList>()
                                data?.workers?.forEach {
                                    workerList.add(WorkerList(it.id, it.hashrate.fixHashRate(wallet.symbol), it.h24.fixHashRate(wallet.symbol)))
                                }
                                initWorkers(workerList)
                            } catch (e: Exception){ catchWorkers() }

                            whatToMine(wallet.symbol, data?.avgHashrate?.h24.fixHashRate(wallet.symbol)) {
                                initBalance(price, unpaid, immature)
                                initEstimated(this)
                                initInfo(whattomine)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            catchSlider()
                            catchBalance()
                            catchWorkers()
                        }
                    }
                    fetchApiNanoPool(wallet.getBaseUrl()).reportedHashRateSharesChart(wallet.address.removeCfx()).onLoaded(this@initMain) {
                        try {
                            val report = data
                            data.forEachIndexed { index, nanopoolHashRateChartData ->
                                report[index].hashrate = nanopoolHashRateChartData.current.fixHashRate(wallet.symbol)
                            }
                            fetchApiNanoPool(wallet.getBaseUrl()).currentHashRateChart(wallet.address.removeCfx()).onLoaded(this@initMain) {
                                try {
                                    data.forEachIndexed { index, nanopoolHashRateChartData ->
                                        report[index].current = nanopoolHashRateChartData.hashrate.fixHashRate(wallet.symbol)
                                    }
                                    initChart(report)
                                } catch (e: Exception){
                                    initChart(null)
                                }
                            }
                        } catch (e: Exception){
                            initChart(null)
                        }
                    }
                    fetchApiNanoPool(wallet.getBaseUrl()).payments(wallet.address.removeCfx()).onLoaded(this@initMain) {
                        try {
                            val payout = mutableListOf<PaymentList>()
                            data.forEach {
                                payout.add(
                                    PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                                        it.txHash, wallet.symbol, it.date)
                                )
                            }
                            initPayments(payout)
                        } catch (e: Exception){
                            catchPayments()
                        }
                    }
                }
                Pool.FLEXPOOL.value -> {
                    fetchApiFlexPool().stats(wallet.symbol, wallet.address).onLoaded(this@initMain) {
                        try {
                            initSliderHash(result.reportedHashrate, result.currentEffectiveHashrate, result.averageEffectiveHashrate)
                            initSliderShares(result.validShares, result.staleShares, result.invalidShares)

                            when(wallet.symbol){
                                Crypto.XCH.name -> chiaFormula(result.averageEffectiveHashrate) {
                                    initEstimated(this)
                                    coinInfo.gone()
                                }
                                else -> whatToMine(wallet.symbol, result.averageEffectiveHashrate) {
                                    initEstimated(this)
                                    initInfo(whattomine)
                                }
                            }
                        } catch (e: Exception){
                            e.printStackTrace()
                            catchSlider()
                            catchEstimated()
                        }
                    }
                    fetchApiFlexPool().chart(wallet.symbol, wallet.address).onLoaded(this@initMain) {
                        try {
                            initChart(this)
                        } catch (e: Exception) {
                            catchChart()
                        }
                    }
                    fetchApiFlexPool().balance(wallet.symbol, wallet.address).onLoaded(this@initMain) {
                        try {
                            initBalanceCrypto(result.balance.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL), emptyString())
                            val balance = result.balance
                            fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(wallet.symbol, currency).onLoaded(this@initMain) {
                                try {
                                    initBalance(this[currency]!!, balance.toCrypto(wallet.symbol,
                                        CryptoFormat.DIVIDE_BLOCK).toBigDecimal(), ConstantLib.ZERO.toBigDecimal())
                                } catch (e: Exception){
                                    e.printStackTrace()
                                }
                            }
                        } catch (e: Exception){
                            e.printStackTrace()
                            catchBalance()
                        }
                    }
                    fetchApiFlexPool().workers(wallet.symbol, wallet.address).onLoaded(this@initMain) {
                        try {
                            val workerList = mutableListOf<WorkerList>()
                            result.forEach {
                                workerList.add(
                                    WorkerList(it.name, it.currentEffectiveHashrate,
                                        it.averageEffectiveHashrate, it.validShares, it.staleShares)
                                )
                            }
                            initWorkers(workerList)
                        } catch (e: Exception) {
                            catchWorkers()
                        }
                    }
                    fetchApiFlexPool().payments(wallet.symbol, wallet.address,0).onLoaded(this@initMain) {
                        try {
                            val payout = mutableListOf<PaymentList>()
                            result.data.forEach {
                                payout.add(PaymentList(it.value, it.hash, wallet.symbol, it.timestamp))
                            }
                            initPayments(payout)
                        } catch (e: Exception) {
                            catchPayments()
                        }
                    }
                }
                Pool.ETHERMINE.value, Pool.FLYPOOL.value -> {
                    immature.root.visibleIf(wallet.symbol != Crypto.ETHW.name)
                    fetchApiBitFly(wallet.getBaseUrl()).payout(wallet.address).onLoaded(this@initMain) {
                        try {
                            val paymentList = mutableListOf<PaymentList>()
                            data.forEach {
                                paymentList.add(PaymentList(it.amount, it.txHash, wallet.symbol, it.paidOn, it.kernelId))
                            }
                            initPayments(paymentList)
                        } catch (e: Exception){
                            catchPayments()
                        }
                    }
                    fetchApiBitFly(wallet.getBaseUrl()).currentStats(wallet.address).onLoaded(this@initMain) {
                        try {
                            initSliderHash(data!!.reportedHashrate, data.currentHashrate, data.averageHashrate)
                            initSliderShares(data.validShares, data.staleShares, data.invalidShares)
                            initBalanceCrypto(data.unpaid.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL),
                                data.unconfirmed.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL))

                            whatToMine(wallet.symbol, data.averageHashrate) {
                                initBalance(price, data.unpaid.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal(),
                                    data.unconfirmed.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK).toBigDecimal())
                                initEstimated(this)
                                initInfo(whattomine)
                            }
                        } catch (e: Exception){
                            catchSlider()
                            catchBalance()
                            catchEstimated()
                        }
                    }
                    fetchApiBitFly(wallet.getBaseUrl()).history(wallet.address).onLoaded(this@initMain) {
                        initChart(this)
                    }
                    fetchApiBitFly(wallet.getBaseUrl()).workers(wallet.address).onLoaded(this@initMain) {
                        try {
                            val workerList = mutableListOf<WorkerList>()
                            data.forEach {
                                workerList.add(
                                    WorkerList(it.worker, it.currentHashrate, it.averageHashrate,
                                        it.validShares, it.staleShares)
                                )
                            }
                            initWorkers(workerList)
                        } catch (e: Exception){
                            catchWorkers()
                        }
                    }
                }
            }
        }
    }
}