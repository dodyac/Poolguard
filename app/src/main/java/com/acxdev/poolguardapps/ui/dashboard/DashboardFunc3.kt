package com.acxdev.poolguardapps.ui.dashboard

import android.content.Intent
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.adapter.ViewPager2Adapter
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.toZero
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.set
import com.acxdev.commonFunction.util.ext.view.setGrid
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.setWrapContent
import com.acxdev.commonFunction.util.ext.view.tint
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Reward
import com.acxdev.poolguardapps.model.Whattomine
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.ui.chart.FragmentHashRate
import com.acxdev.poolguardapps.ui.chart.FragmentShares
import com.acxdev.poolguardapps.ui.payout.ActivityPayouts
import com.acxdev.poolguardapps.ui.payout.RowPayout
import com.acxdev.poolguardapps.ui.worker.ActivityWorkers
import com.acxdev.poolguardapps.ui.worker.RowWorker
import com.acxdev.poolguardapps.util.CryptoFormat
import com.acxdev.poolguardapps.util.balloonText
import com.acxdev.poolguardapps.util.calculateLocalCurrency
import com.acxdev.poolguardapps.util.formatReadable
import com.acxdev.poolguardapps.util.initChartHashShares
import com.acxdev.poolguardapps.util.invalidateFormat
import com.acxdev.poolguardapps.util.paymentOnClick
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toLocalCurrency
import com.acxdev.poolguardapps.util.whatToMine
import com.acxdev.poolguardapps.util.workerOnClick
import com.acxdev.poolguardapps.util.zeroCrypto
import com.acxdev.poolguardapps.util.zeroLocalCurrency
import com.skydoves.balloon.ArrowOrientation
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

object DashboardFunc3 {

    fun ActivityDashboard.initBalanceCrypto(
        unpaids: String,
        immatures: String
    ) {
        scopeDashboard {
            unpaid.crypto.text = unpaids
            immature.crypto.text = immatures
        }
    }

    fun ActivityDashboard.initBalance(
        price: Double,
        unpaids: BigDecimal,
        immatures: BigDecimal?
    ) {
        scopeDashboard {
            unpaid.currency.text = unpaids.calculateLocalCurrency(price, this@initBalance)
            immature.currency.text = immatures?.calculateLocalCurrency(price, this@initBalance) ?: zeroLocalCurrency()
            coinExchange.text = if(wallet.symbol == Crypto.SHIB.name) {
                price.toLocalCurrency(this@initBalance, 8)
            } else {
                price.toLocalCurrency(this@initBalance)
            }
        }
    }

    fun ActivityDashboard.initInfo(
        whattomine: Whattomine?,
    ) {
        scopeDashboard {
            coinInfo.visibleIf(wallet.symbol != Crypto.SHIB.name)
            if(wallet.symbol != Crypto.SHIB.name) {
                netHash.value.text = whattomine?.nethash.formatReadable()
                diff.value.text = whattomine?.difficulty.formatReadable().removeSuffix("H/s")
                block.value.text = NumberFormat.getInstance().format(whattomine?.last_block)
            }
        }
    }

    fun ActivityDashboard.initSliderHash(
        reported: Float?,
        current: Float?,
        average: Float?
    ) {
        scopeDashboard {
            val hashRate = mutableListOf<Menu>()
            if (wallet.pool != Pool.HEROMINERS.value)
                if (wallet.symbol == Crypto.ETHW.name || wallet.symbol == Crypto.ETC.name)
                    hashRate.add(Menu(getString(R.string.reported), reported.invalidateFormat(wallet.symbol)))
            hashRate.add(Menu(getString(R.string.current), current.invalidateFormat(wallet.symbol)))
            hashRate.add(Menu(getString(R.string.average), average.invalidateFormat(wallet.symbol)))
            sliderHashRate.sliderView.set(RowSlide(hashRate))
        }
    }

    fun ActivityDashboard.initSliderShares(
        valid: Float?,
        stale: Float?,
        invalid: Float?
    ) {
        scopeDashboard {
            val shares = mutableListOf<Menu>()
            shares.add(Menu(getString(R.string.valid), DecimalFormat("0.##").format(valid ?: 0)))
            shares.add(Menu(getString(R.string.stale), DecimalFormat("0.##").format(stale ?: 0)))
            shares.add(Menu(getString(R.string.invalid), DecimalFormat("0.##").format(invalid ?: 0)))
            sliderShares.sliderView.set(RowSlide(shares))
        }
    }

    fun <T> ActivityDashboard.initChart(
        data: T
    ) {
        scopeDashboard {
            initChartHashShares(chartShimmer, chart, wallet, data)
        }
    }

    fun ActivityDashboard.initPayments(
        list: MutableList<PaymentList>
    ) {
        scopeDashboard {
            val adapter = RowPayout(list, false, wallet, paymentOnClick(wallet))
            payout.setVStack(adapter)
            if (list.size > Constant.DefaultValue.MAX_PAYMENTS) {
                viewAllPayout.visible()
                viewAllPayout.setOnClickListener {
                    Intent(this@initPayments, ActivityPayouts::class.java)
                        .putExtra(Constant.Intent.ID, wallet._id.toString())
                        .also {
                            startActivity(it)
                        }
                }
            } else if (list.isEmpty()) {
                payout.hideShimmer()
                payoutLayout.gone()
            }
        }
    }

    fun ActivityDashboard.initWorkers(
        list: MutableList<WorkerList>
    ) {
        scopeDashboard {
            worker.value.text = list.size.toZero()
            worker.text.text = if (list.size > 1) getString(R.string.workers) else getString(R.string.worker)
            activeWorkers.text = if (list.size > 1) getString(R.string.activeWorkersNum, list.size) else getString(R.string.activeWorkerNum, list.size)

            val adapter = RowWorker(list, false, wallet, workerOnClick(wallet))
            workers.setGrid(adapter, 200F)
            if (list.size > Constant.DefaultValue.MAX_WORKERS) {
                viewAllWorkers.visible()
                viewAllWorkers.setOnClickListener {
                    Intent(this@initWorkers, ActivityWorkers::class.java)
                        .putExtra(Constant.Intent.ID, wallet._id.toString())
                        .also {
                            startActivity(it)
                        }
                }
            } else if (list.isEmpty()) {
                workers.hideShimmer()
                workerLayout.gone()
            }
        }
    }

    fun ActivityDashboard.initEstimated(
        reward: Reward
    ) {
        scopeDashboard {
            estimatedCard.visible()

            estimatedInfo.setOnClickListener {
                balloonText(
                    ArrowOrientation.TOP,
                    getString(R.string.estimatedEarningsByHashRate)
                ).showAlignBottom(it)
            }

            var current = 0
            estimated(reward.price, reward.hourly, R.string.hourly)

            estimatedSwitch.setOnClickListener {
                current += 1
                if (current == 5) current = 0
                when (current) {
                    0 -> estimated(reward.price, reward.hourly, R.string.hourly)
                    1 -> estimated(reward.price, reward.daily, R.string.daily)
                    2 -> estimated(reward.price, reward.weekly, R.string.weekly)
                    3 -> estimated(reward.price, reward.monthly, R.string.monthly)
                    4 -> estimated(reward.price, reward.yearly, R.string.yearly)
                }
            }
        }
    }

    fun ActivityDashboard.estimated(
        price: Double,
        reward: Double,
        text: Int
    ) {
        scopeDashboard {
            estimated.crypto.text = reward.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL)
            estimated.currency.text = reward.calculateLocalCurrency(price, this@estimated)
            estimated.text.text = getString(text)
        }
    }

    fun ActivityDashboard.catchWallet() {
        scopeDashboard {
            wentWrong.visible()
            layout.gone()
        }
    }

    fun ActivityDashboard.catchPayments() {
        scopeDashboard {
            payout.hideShimmer()
            payoutLayout.gone()
        }
    }

    fun ActivityDashboard.catchWorkers() {
        scopeDashboard {
            worker.value.text = ConstantLib.ZERO
            worker.text.text = getString(R.string.worker)
            workers.hideShimmer()
            workerLayout.gone()
        }
    }

    fun ActivityDashboard.catchSlider() {
        initSliderHash(0F, 0F, 0F)
        initSliderShares(0F, 0F, 0F)
    }

    fun ActivityDashboard.catchEstimated() {
        scopeDashboard {
            estimatedCard.gone()
        }
    }

    fun ActivityDashboard.catchChart() {
        scopeDashboard {
            chartShimmer.root.gone()
            chart.root.visible()
            val pager2Adapter = ViewPager2Adapter(this@catchChart)
            val type = if (wallet.symbol == Crypto.XCH.name) getString(R.string.space) else getString(R.string.hashrate)
            val mutableListFragment = mutableListOf<Pair<String, Fragment>>()
            mutableListFragment.add(Pair(type, FragmentHashRate()))
            mutableListFragment.add(Pair(getString(R.string.shares), FragmentShares()))
            chart.viewPager.adapter = pager2Adapter
            pager2Adapter.setWithTab(
                mutableListFragment,
                chart.tabLayout,
                chart.viewPager
            )
            chart.viewPager.setWrapContent()
        }
    }

    fun ActivityDashboard.catchBalance() {
        scopeDashboard {
            unpaid.crypto.text = zeroCrypto(wallet.symbol)
            unpaid.currency.text = zeroLocalCurrency()
            immature.crypto.text = zeroCrypto(wallet.symbol)
            immature.currency.text = zeroLocalCurrency()
            whatToMine(wallet.symbol, 0F) {
                coinExchange.text = if(wallet.symbol == Crypto.SHIB.name) {
                    price.toLocalCurrency(this@catchBalance, 8)
                } else {
                    price.toLocalCurrency(this@catchBalance)
                }
                initInfo(whattomine)
            }
        }
    }

    fun ActivityDashboard.resetBalance() {
        scopeDashboard {
            unpaid.crypto.showShimmer()
            unpaid.currency.showShimmer()
            immature.crypto.showShimmer()
            immature.currency.showShimmer()
        }
    }

    fun ActivityDashboard.resetEstimated() {
        scopeDashboard {
            estimatedCard.visible()

            estimated.crypto.showShimmer()
            estimated.currency.showShimmer()
            estimated.text.showShimmer()
        }
    }

    fun ActivityDashboard.resetChart() {
        scopeDashboard {
            chartShimmer.root.visible()
            chart.root.gone()
        }
    }

    fun ActivityDashboard.resetWorkers() {
        scopeDashboard {
            workers.showShimmer()
            worker.value.showShimmer()
            workerLayout.visible()
            viewAllWorkers.gone()
        }
    }

    fun ActivityDashboard.resetPayments() {
        scopeDashboard {
            payout.showShimmer()
            payoutLayout.visible()
            viewAllPayout.gone()
        }
    }

    fun ActivityDashboard.resetCoinPrice() {
        scopeDashboard {
            coinPTC.text = emptyString()
            coinPTC.setTextColor(getColor(R.color.text))
            arrow.tint(getColor(R.color.cardBackground))
            arrow.rotation = 0F
        }
    }
}
