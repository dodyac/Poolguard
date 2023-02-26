package com.acxdev.poolguardapps.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.BaseAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.decor.LinePagerIndicatorDecoration
import com.acxdev.commonFunction.util.ext.getVersionName
import com.acxdev.commonFunction.util.ext.showSheetWithExtra
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.util.CryptoFormat
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.util.calculateLocalCurrency
import com.acxdev.poolguardapps.util.chiaFormula
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.repository.PoolStatus
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.FragmentHomeBinding
import com.acxdev.poolguardapps.util.fixHashRate
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.invalidateFormat
import com.acxdev.poolguardapps.model.Estimated
import com.acxdev.poolguardapps.model.News
import com.acxdev.poolguardapps.model.Reward
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.coin.CoinChart
import com.acxdev.poolguardapps.rest.EzilType
import com.acxdev.poolguardapps.util.pool
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.removeSuffixPool
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.fetchApiBitFly
import com.acxdev.poolguardapps.rest.fetchApiEzil
import com.acxdev.poolguardapps.rest.fetchApiFlexPool
import com.acxdev.poolguardapps.rest.fetchApiHiveOn
import com.acxdev.poolguardapps.rest.fetchApiNanoPool
import com.acxdev.poolguardapps.rest.fetchApiOtherPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.main.NewsViewModel
import com.acxdev.poolguardapps.util.openWebView
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.whatToMine
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import java.util.*

@SuppressLint("ResourceType")
class FragmentHome : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    lateinit var runnable: Runnable
    lateinit var handler: Handler
    var second = Constant.DefaultValue.DURATION_AUTO_CHECK

    private val estimatedList = mutableListOf<Estimated>()
    private lateinit var estimatedAdapter: RowEstimated
    private val newsViewModel: NewsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.clear()
        handler = Handler(Looper.getMainLooper())
    }

    override fun FragmentHomeBinding.configureViews() {
        handler = Handler(Looper.getMainLooper())

        if(prefs(Constant.SharedPrefs.WHATS_NEW).isEmpty()) {
            safeContext {
                showSheetWithExtra(SheetWhatsNew())
                putPrefs(getVersionName(),"x")
            }
        }

        estimated.addItemDecoration(LinePagerIndicatorDecoration(getString(R.color.primaryBlue), getString(R.color.blueSecondary)))

        init()

        runnable = object : Runnable {
            override fun run() {
                if(second>0){
                    second -= 1
                    autoCheck.text = getString(R.string.updateIn, second)
                    handler.postDelayed(this, Constant.DefaultValue.DELAY_UPDATE_VALUE)
                } else{
                    init()
                    second = Constant.DefaultValue.DURATION_AUTO_CHECK
                    runnable.run()
                }
            }
        }
    }

    override fun FragmentHomeBinding.onClickListener() {
        autoCheck.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                runnable.run()
            } else{
                autoCheck.text = getString(R.string.autoUpdate)
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun FragmentHomeBinding.init() {
        initEstimated()
        initMineableCoin()
        initNews()
    }

    private fun FragmentHomeBinding.initEstimated() {
        estimatedList.clear()
        estimatedAdapter = RowEstimated(estimatedList)

        estimated.setHStack(estimatedAdapter,true, hasFixed = true)
        estimated.visible()
        estimated.showShimmer()

        safeContext {
            val walletList = sqLiteZSelectTable(Wallet::class.java)
            val maxNum = if(walletList.size > 5) 5 else walletList.size
            if(walletList.isEmpty()) {
                defaultEstimated()
            } else for (i in 0 until maxNum) when(walletList[i].pool) {
                Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value,
                Pool.SOLOPOOL.value, Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value, Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value -> {
                    fetchApiOtherPool().minerStatsMain("${walletList[i].getBaseUrl()}accounts/${walletList[i].address}").onLoaded(this) {
                        localCurrency(walletList[i], currentHashrate)
                    }
                }
                Pool.FLOCKPOOL.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).flockPoolDashboard(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], workers.map { it.hashrate.now }.sum())
                }
                Pool.UNMINEABLE.value -> defaultEstimated()
                Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).minerPool(walletList[i].address, 1).onLoaded(this) {
                    localCurrency(walletList[i], hashAvg5m)
                }
                Pool.EZIL.value, Pool.EZIL_ZIL.value -> fetchApiEzil(EzilType.STATS).currentStats(walletList[i].address).onLoaded(this) {
                    val current = if(walletList[i].symbol == Crypto.ETHW.name) eth.current_hashrate else etc.current_hashrate
                    localCurrency(walletList[i], current)
                }
                Pool.HIVEON.value -> fetchApiHiveOn().overview(walletList[i].address.remove0x(), walletList[i].symbol).onLoaded(this) {
                    localCurrency(walletList[i], hashrate)
                }
                Pool.RAVENMINER.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).ravenMiner(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], hashrate.the5Min)
                }
                Pool.MINEXMR.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).mineXmrWorker(walletList[i].address).onLoaded(this) {
                    val hashrate = sumOf { it.hashrate.toDouble() }.toFloat()
                    localCurrency(walletList[i], hashrate)
                }
                Pool.WOOLYPOOLYSOLO.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).woolyPooly(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], mode_stats.solo.default.currentHashrate)
                }
                Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).k1Pool(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], miner.curHashrate)
                }
                Pool.LUCKPOOL.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).luckPool(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], hashrateSols)
                }
                Pool.WOOLYPOOLY.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).woolyPooly(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], mode_stats.pplns.default.currentHashrate)
                }
                Pool.HEROMINERS.value -> fetchApiOtherPool(walletList[i].getBaseUrl()).heroMiners(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], stats.hashrate)
                }
                Pool.NANOPOOL.value -> fetchApiNanoPool(walletList[i].getBaseUrl()).stats(walletList[i].address.removeCfx()).onLoaded(this) {
                    localCurrency(walletList[i], data?.hashrate.fixHashRate(walletList[i].symbol))
                }
                Pool.FLEXPOOL.value -> fetchApiFlexPool().stats(walletList[i].symbol, walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], result.currentEffectiveHashrate)
                }
                Pool.ETHERMINE.value, Pool.FLYPOOL.value -> fetchApiBitFly(walletList[i].getBaseUrl()).currentStats(walletList[i].address).onLoaded(this) {
                    localCurrency(walletList[i], data?.currentHashrate)
                }
            }
        }
    }

    private fun FragmentHomeBinding.initMineableCoin() {
        val topMineAbleCoin = listOf(
            Crypto.BTC,
            Crypto.ETC,
            Crypto.ETHW,
            Crypto.FLUX,
            Crypto.RVN,
        )
        val cryptoNameList = topMineAbleCoin.joinToString { it.name }.replace(" ", "")

        safeContext {
            fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getPriceMultiFull(cryptoNameList, currency).onLoaded(this) {
                val coinList = mutableListOf<CoinChart>()
                val adapter = RowCoinChart(coinList, RAW)
                coin.setHStack(adapter, hasFixed = true)
                coin.showShimmer()

                topMineAbleCoin.forEach {
                    val coinId = if(it == Crypto.ETHW) {
                        "ethereum-pow-iou"
                    } else {
                        it.fullName.lowercase().replace(" ", "-")
                    }

                    fetchApi(BaseUrl.URL_COINSTATS).charts("24h", coinId).onLoaded(this@safeContext) {
                        coinList.add(CoinChart(it.name, chart))
                        adapter.notifyDataSetChanged()
                        coin.hideShimmer()
                    }
                }
            }
        }
    }

    private fun FragmentHomeBinding.localCurrency(
        wallet: Wallet,
        hashRate: Float?
    ) {
        try {
            safeContext {
                when(wallet.symbol){
                    Crypto.XCH.name -> chiaFormula(hashRate) {
                        addEstimated(wallet, hashRate,this, this@safeContext)
                    }
                    else -> whatToMine(wallet.symbol, hashRate) {
                        addEstimated(wallet, hashRate,this, this@safeContext)
                    }
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun FragmentHomeBinding.addEstimated(
        wallet: Wallet,
        hashRate: Float?,
        reward: Reward,
        context: Context
    ) {
        val fee = wallet.pool().fee
        val profitDaily = reward.daily * ((100 - fee) / 100)
        val profitMonthly = reward.monthly * ((100 - fee) / 100)
        try {
            estimatedList.add(Estimated(profitDaily.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL), profitDaily.calculateLocalCurrency(reward.price, context),
                "${hashRate.invalidateFormat(wallet.symbol)} on ${wallet.pool.removeSuffixPool()}", profitMonthly.toCrypto(wallet.symbol, CryptoFormat.FORMAT_SYMBOL),
                profitMonthly.calculateLocalCurrency(reward.price, context), wallet))
            estimatedAdapter.notifyDataSetChanged()
        } catch (e: Exception){ e.printStackTrace() }
        estimated.hideShimmer()
    }

    private fun FragmentHomeBinding.defaultEstimated() {
        val availablePool = Pool.values().toList().filter { it.isListed && it.poolStatus == PoolStatus.ACTIVE }
        val defPool = availablePool[Random().nextInt(availablePool.size)]
        val defWallet = Wallet()

        defWallet.pool = defPool.value.removeSuffixPool()
        defWallet.symbol = if(defPool.coin.isNotEmpty()) {
            defPool.coin[Random().nextInt(defPool.coin.size)].crypto.name
        } else {
            Crypto.ETHW.name
        }
        val randomHash = (30000000..100000000).random().toFloat()
        localCurrency(defWallet, randomHash)
    }

    private fun FragmentHomeBinding.initNews() {
        lifecycleScope.launchWhenStarted {
            newsViewModel.newsResponse.collect {
                if(it is NewsViewModel.NewsResponseEvent.Success) {
                    news.setVStack(RowNews(it.data,false, object : OnClick<News> {
                        override fun onItemClick(item: News, position: Int) {
                            safeContext {
                                openWebView(item.url)
                            }
                        }
                    }), hasFixed = true)
                }
            }
        }
    }
}