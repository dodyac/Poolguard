package com.acxdev.poolguardapps.ui.main.profitability

import android.content.Intent
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.transition.TransitionManager
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.showSheetWithExtra
import com.acxdev.commonFunction.util.ext.toZero
import com.acxdev.commonFunction.util.ext.view.set
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.commonFunction.util.ext.view.setText
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.toEditString
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.FragmentProfitabilityBinding
import com.acxdev.poolguardapps.model.Profit
import com.acxdev.poolguardapps.model.coin.Coin
import com.acxdev.poolguardapps.model.gpu.AlgorithmStats
import com.acxdev.poolguardapps.model.gpu.GPUDatabase
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.GPU.gpuDefault
import com.acxdev.poolguardapps.repository.GPU.gpuStats
import com.acxdev.poolguardapps.repository.PoolCoinList
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.gpu.ActivityGPU
import com.acxdev.poolguardapps.ui.pool.RowCoin
import com.acxdev.poolguardapps.util.*
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import com.skydoves.balloon.ArrowOrientation

class FragmentProfitability : BaseFragment<FragmentProfitabilityBinding>(FragmentProfitabilityBinding::inflate) {

    private lateinit var symbol: String

    override fun FragmentProfitabilityBinding.configureViews() {
        safeContext {
            gpuDefault()

            //TODO Raw String
            type.setVStack(RowTabLayout(mutableListOf("GPU (BETA)", getString(R.string.manual)), object : OnClick<String>{
                override fun onItemClick(item: String, position: Int) {
                    putPrefs(Constant.SharedPrefs.CALCULATOR_STATE, position)
                    TransitionManager.beginDelayedTransition(frame)
                    initLayout()
                }

            }), spanCount = 2)
        }

        initLayout()

        layoutManual()

        layoutGpu()
    }

    override fun FragmentProfitabilityBinding.onClickListener() {

    }

    private fun initClickCalculate() {
        scopeLayout {
            safeContext {
                if(powerCostGpu.isNotEmpty()) {
                    putPrefs(Constant.SharedPrefs.POWER_COST_GPU_STATE, powerCostGpu.toEditString())

                    showDialog()

                    Crypto.values().toList()
                        .filter { it.algorithm.name == prefs(Constant.SharedPrefs.ALGORITHM_STATE) && it.isListed }
                        .map { it.name }
                        .getPriceMultiFull()
                }
            }
        }
    }

    private fun GPUDatabase.filterAlgorithm(): List<AlgorithmStats> {
        return gpuStats().algorithm.filter { it.algorithm.name == prefs(Constant.SharedPrefs.ALGORITHM_STATE) }
    }
    
    private fun List<String>.getPriceMultiFull() {
        scopeLayout {
            safeContext {
                fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(Constant.DefaultValue.USD, currency).onLoaded(this) {
                    val price = this[currency]!!
                    val cost = prefs(Constant.SharedPrefs.POWER_COST_GPU_STATE).toDouble() / price

                    val coins = mutableListOf<Profit>()
                    val profitAdapter = RowProfit(coins, price)
                    profits.setVStack(profitAdapter)

                    this@getPriceMultiFull.forEach {
                        it.profitability(cost, coins, profitAdapter)
                    }
                }
            }
        }
    }

    private fun String.profitability(cost: Double, list: MutableList<Profit>, profitAdapter: RowProfit) {
        safeContext {
            val checked = sqLiteZSelectTable(GPUDatabase::class.java).filter { it.isChecked == 1 }

            val totalHash = checked.sumOf { it.filterAlgorithm().sumOf { alg -> alg.hashRate } * it.count }

            val totalPower = checked.sumOf { it.filterAlgorithm().sumOf { alg -> alg.power } * it.count }

            fetchApi(BaseUrl.URL_WHATTOMINE).gpuCalculator(crypto().whatToMineCode, totalHash, totalPower, cost,
                prefs(Constant.SharedPrefs.BLOCK_REWARD_STATE), prefs(Constant.SharedPrefs.DIFFICULTY_STATE)).onLoaded(this) {
                dismissDialog()
                list.add(Profit(tag, algorithm, revenue, profit))
                profitAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun estimated(symbol: String, price: Double, reward: Double, text: Int, power: Double) {
        scopeLayout {
            estimated.crypto.text = reward.toCrypto(symbol, CryptoFormat.FORMAT_SYMBOL)
            val revenue = reward * price

            val x = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x0dp), LinearLayout.LayoutParams.WRAP_CONTENT)

            safeContext {
                if(fees.toEditString().isNotEmpty()){
                    val fees = revenue * (fees.toEditString().toDouble() / 100)

                    val p = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x0dp), LinearLayout.LayoutParams.WRAP_CONTENT)
                    p.weight = fees.toFloat()
                    poolFee.layoutParams = p

                    val profits = revenue - fees - power
                    x.weight = profits.toFloat()
                    profit.layoutParams = x

                    poolFeeText.text = getString(R.string.fees, fees.toLocalCurrency(this))
                    profitText.text = getString(R.string.profit, profits.toLocalCurrency(this))
                } else {
                    val profits = revenue - power

                    x.weight = profits.toFloat()
                    profit.layoutParams = x

                    poolFeeText.text = getString(R.string.fees, zeroLocalCurrency())
                    profitText.text = getString(R.string.profit, profits.toLocalCurrency(this))
                }

                val p = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x0dp), LinearLayout.LayoutParams.WRAP_CONTENT)
                p.weight = power.toFloat()
                powerCostx.layoutParams = p

                estimated.currency.text = revenue.toLocalCurrency(this)
                estimated.text.text = getString(text)
                powerCostText.text = getString(R.string.powerCost, power.toLocalCurrency(this))
            }
        }
    }

    private fun initLayout() {
        scopeLayout {
            val state = prefs(Constant.SharedPrefs.CALCULATOR_STATE).toInt() == 1
            manual.visibleIf(state)
            gpu.visibleIf(!state)
        }
    }

    private fun layoutManual() {
        Constant.SELECTED_COIN = 0
        symbol = Crypto.ETHW.name
        val coinList = PoolCoinList.profitabilityCoin().toMutableList()

        scopeLayout {
            unit.set(R.array.unitHash)

            pinnedSwitch.setImageResource(if(prefs(Constant.SharedPrefs.PINNED_PROFITABILITY_STATE).toBoolean()) R.drawable.icons8_unpin else R.drawable.icons8_push_pin)

            if(prefs(Constant.SharedPrefs.PINNED_PROFITABILITY_STATE).toBoolean()) {
                val currentCoin = Coin(prefs(Constant.SharedPrefs.COIN_STATE).crypto(), emptyString())
                coinList.remove(currentCoin)
                coinList.add(0, currentCoin)

                hashRate.setText(prefs(Constant.SharedPrefs.HASHRATE_STATE))
                unit.setText(prefs(Constant.SharedPrefs.UNIT_STATE), false)

                power.setText(prefs(Constant.SharedPrefs.POWER_STATE))
                powerCost.setText(prefs(Constant.SharedPrefs.COST_STATE))
                fees.setText(prefs(Constant.SharedPrefs.FEE_STATE))

                estimatedCard.visible()

                initWhatToMine(currentCoin.crypto.name, false)
            }

            safeContext {
                pinnedSwitch.setOnClickListener {
                    if (prefs(Constant.SharedPrefs.PINNED_PROFITABILITY_STATE).toBoolean()) {
                        pinnedSwitch.setImageResource(R.drawable.icons8_push_pin)
                        putPrefs(Constant.SharedPrefs.PINNED_PROFITABILITY_STATE, false)
                    } else {
                        pinnedSwitch.setImageResource(R.drawable.icons8_unpin)
                        putPrefs(Constant.SharedPrefs.PINNED_PROFITABILITY_STATE, true)
                    }
                }

                coin.setHStack(RowCoin(coinList, object : OnClick<Coin> {
                    override fun onItemClick(item: Coin, position: Int) { symbol = item.crypto.name }
                }), hasFixed = true)

                powerCost.suffixText = "${currency}/kWh"

                estimated.crypto.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.acxdev.commonFunction.R.dimen.text_header))

                calculate.setOnClickListener {
                    if(hashRate.isNotEmpty()){
                        putPrefs(Constant.SharedPrefs.COIN_STATE, symbol)

                        putPrefs(Constant.SharedPrefs.HASHRATE_STATE, hashRate.toEditString())
                        putPrefs(Constant.SharedPrefs.UNIT_STATE, unit.text.toString())

                        putPrefs(Constant.SharedPrefs.POWER_STATE, power.toEditString())
                        putPrefs(Constant.SharedPrefs.COST_STATE, powerCost.toEditString())
                        putPrefs(Constant.SharedPrefs.FEE_STATE, fees.toEditString())

                        initWhatToMine(symbol)
                    }
                }
            }
        }
    }

    private fun initWhatToMine(symbol: String, progress: Boolean = true) {
        scopeLayout {
            safeContext {
                val hashRate: Float = when(unit.text.toString()){
                    "H/s" -> hashRate.toEditString().toFloat()
                    "KH/s" -> hashRate.toEditString().toFloat() * 1000F
                    "MH/s" -> hashRate.toEditString().toFloat() * 1000000F
                    "GH/s" -> hashRate.toEditString().toFloat() * 1000000000F
                    "TH/s" -> hashRate.toEditString().toFloat() * 1000000000000F
                    else -> hashRate.toEditString().toFloat()
                }

                whatToMine(symbol, hashRate, progress){
                    estimatedCard.visible()

                    estimatedInfo.setOnClickListener {
                        balloonText(
                            ArrowOrientation.TOP,
                            getString(R.string.estimatedEarningsInfo)
                        ).showAlignBottom(it)
                    }

                    val powerCostHourly = power.toEditString().toZero().toDouble() / 1000 * powerCost.toEditString().toZero().toDouble()
                    val powerCostDaily = powerCostHourly * 24
                    val powerCostWeekly = powerCostDaily * 7
                    val powerCostMonthly = powerCostDaily * 30
                    val powerCostYearly = powerCostDaily * 365

                    var current = 0
                    estimated(symbol, price, daily, R.string.daily, powerCostDaily)

                    estimatedSwitch.setOnClickListener {
                        current += 1
                        if(current == 4) current = 0
                        when(current){
                            0 -> estimated(symbol, price, daily, R.string.daily, powerCostDaily)
                            1 -> estimated(symbol, price, weekly, R.string.weekly, powerCostWeekly)
                            2 -> estimated(symbol, price, monthly, R.string.monthly, powerCostMonthly)
                            3 -> estimated(symbol, price, yearly, R.string.yearly, powerCostYearly)
                        }
                    }
                }
            }
        }
    }

    private fun layoutGpu() {
        scopeLayout {
            safeContext {
                powerCostGpu.suffixText = "${currency}/kWh"
                powerCostGpu.setText(prefs(Constant.SharedPrefs.POWER_COST_GPU_STATE))

                device.setOnClickListener {
                    Intent(this, ActivityGPU::class.java)
                        .also {
                            startActivity(it)
                        }
                }

                filter.setOnClickListener {
                    showSheetWithExtra(SheetFilter(object : SheetFilter.OnSheetFilter{
                        override fun onComplete() {
                            initClickCalculate()
                        }
                    }))
                }

                calculateGpus.setOnClickListener {
                    initClickCalculate()
                }
            }
        }
    }
}