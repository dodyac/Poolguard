package com.acxdev.poolguardapps.ui.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.showSheetWithExtra
import com.acxdev.commonFunction.util.ext.view.setFlex
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.commonFunction.util.toasty
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityDashboardBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.repository.PoolStatus
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc.initMain
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc2.initObserve
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc2.initUI
import com.acxdev.poolguardapps.ui.dashboard.DashboardFunc3.catchWallet
import com.acxdev.poolguardapps.ui.main.ActivityMain
import com.acxdev.poolguardapps.ui.main.setting.SheetRecycler
import com.acxdev.poolguardapps.ui.pool.RowPool
import com.acxdev.poolguardapps.ui.wallet.RowMenu
import com.acxdev.poolguardapps.util.balloonMenu
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.openWebView
import com.acxdev.poolguardapps.util.pool
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeAPI
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.removeSuffixPool
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZGetById
import com.acxdev.sqlitez.SqliteZ.Companion.sqliteZUpdate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.util.*

class ActivityDashboard : BaseActivity<ActivityDashboardBinding>(ActivityDashboardBinding::inflate) {

    lateinit var runnable: Runnable
    lateinit var handler: Handler
    var second = Constant.DefaultValue.DURATION_AUTO_CHECK
    lateinit var currency: String
    lateinit var wallet: Wallet

    var looper = 0

    lateinit var bottomSheetMigrate: BottomSheetDialogFragment

    fun scopeDashboard(viewBinding: (ActivityDashboardBinding.() -> Unit)) {
        scopeLayout {
            viewBinding.invoke(this)
        }
    }

    override fun ActivityDashboardBinding.configureViews() {
        currency = prefs(Constant.SharedPrefs.CURRENCY)

        handler = Handler(Looper.getMainLooper())
        try {
            wallet = sqLiteZGetById(Wallet::class.java, intent.getStringExtra(Constant.Intent.ID)!!.toLong())

            toolbar.setWithMenu(wallet.name) {

                val menuDashboard = balloonMenu(this)
                val list = mutableListOf<String>()
                list.add(getString(R.string.viewOn, wallet.pool.removeSuffixPool()))
                list.add(getString(R.string.migrate))
                list.add(getString(R.string.copyAddress))
                list.add(getString(R.string.deleteWallet))
                menuDashboard.getContentView().findViewById<RecyclerView>(R.id.recycler)
                    .setVStack(RowMenu(list, object : OnClick<String> {
                        override fun onItemClick(item: String, position: Int) {
                            when (position) {
                                0 -> when (wallet.pool) {
                                    Pool.FLOCKPOOL.value -> openWebView("https://flockpool.com/miners/rtm/${wallet.address}")
                                    Pool.UNMINEABLE.value -> openWebView("https://unmineable.com/coins/${wallet.symbol}/address/${wallet.address}")
                                    Pool.MINERPOOLSOLO.value -> openWebView("https://solo-${wallet.symbol.lowercase()}.minerpool.org/workers/${wallet.address}")
                                    Pool.MINERPOOL.value -> openWebView("https://${wallet.symbol.lowercase()}.minerpool.org/workers/${wallet.address}")
                                    Pool.EZIL.value, Pool.EZIL_ZIL.value -> openWebView("https://ezil.me/personal_stats?wallet=${wallet.address}&coin=${wallet.symbol.lowercase()}")
                                    Pool.HIVEON.value -> openWebView("https://hiveon.net/${wallet.symbol.lowercase()}?miner=${wallet.address.remove0x()}")
                                    Pool.RAVENMINER.value -> openWebView("${wallet.getBaseUrl().removeAPI().removeSuffix("/")}wallets/${wallet.address}")
                                    Pool.COMINERS.value -> openWebView("${wallet.getBaseUrl().removeAPI().removeSuffix("/")}account/${wallet.address}")
                                    Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> openWebView("${wallet.getBaseUrl().removeAPI()}#/account/${wallet.address}")
                                    Pool.MINEXMR.value -> openWebView("https://minexmr.com/dashboard?address=${wallet.address}")
                                    Pool.ETHO.value -> openWebView("${wallet.getBaseUrl().removeAPI()}#/account/${wallet.address}")
                                    Pool.BAIKALMINESOLO.value -> openWebView("https://baikalmine.com/pools/solo/${wallet.symbol.lowercase()}/miners/${wallet.address}")
                                    Pool.BAIKALMINEPPS.value -> openWebView("https://baikalmine.com/pools/pps_plus/${wallet.symbol.lowercase()}/miners/${wallet.address}")
                                    Pool.BAIKALMINE.value -> openWebView("https://baikalmine.com/pools/pplns/${wallet.symbol.lowercase()}/miners/${wallet.address}")
                                    Pool.SOLOPOOL.value -> openWebView("${wallet.getBaseUrl().removeAPI().removeSuffix("/")}account/${wallet.address}")
                                    Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value ->
                                        openWebView("${wallet.getBaseUrl().replace("api/miner/", "pool/")}account/${wallet.address}")
                                    Pool.LUCKPOOL.value -> openWebView(wallet.getBaseUrl().removeAPI())
                                    Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> openWebView("${wallet.getBaseUrl().removeAPI().removeSuffix("/")}account/${wallet.address}")
                                    Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> openWebView("${wallet.getBaseUrl().removeAPI()}#/account/${wallet.address}")
                                    Pool.CRAZYPOOL.value -> openWebView("${wallet.getBaseUrl().removeAPI()}#/account/${wallet.address}")
                                    Pool.WOOLYPOOLY.value, Pool.WOOLYPOOLYSOLO.value -> openWebView("https://woolypooly.com/en/coin/${wallet.symbol.lowercase()}/wallets/${wallet.address}")
                                    Pool.HEROMINERS.value -> openWebView("${wallet.getBaseUrl().removeAPI().removeSuffix("/")}?wallets=${wallet.address}")
                                    Pool.NANOPOOL.value -> {
                                        when (wallet.symbol) {
                                            Crypto.ERG.name -> openWebView("https://ergo.nanopool.org/account/${wallet.address}")
                                            Crypto.CFX.name -> openWebView("https://cfx.nanopool.org/account/${wallet.address.removeCfx()}")
                                            else -> openWebView("https://${wallet.symbol}.nanopool.org/account/${wallet.address}")
                                        }
                                    }
                                    Pool.FLEXPOOL.value -> openWebView("${wallet.getBaseUrl().removeAPI()}miner/${wallet.symbol.lowercase()}/${wallet.address}/stats")
                                    Pool.ETHERMINE.value, Pool.FLYPOOL.value -> openWebView("${wallet.getBaseUrl().removeAPI()}miners/${wallet.address}/dashboard")
                                }
                                1 -> {
                                    bottomSheetMigrate = SheetRecycler(R.string.migrate) { recyclerView, emptyView ->
                                        Constant.SELECTED_COIN = -1

                                        val filteredPool = mutableListOf<Pool>()

                                        Pool.values()
                                            .filter { it.isListed && it.poolStatus == PoolStatus.ACTIVE }.forEach { pool ->
                                                pool.coin.forEach { coin ->
                                                    if (coin.crypto.name == wallet.symbol) {
                                                        filteredPool.add(pool)
                                                    }
                                                }
                                            }

                                        filteredPool.remove(wallet.pool())
                                        filteredPool.remove(Pool.EZIL_ZIL)

                                        if(wallet.pool == Pool.EZIL_ZIL.value) filteredPool.clear()

                                        //TODO Raw String
                                        emptyView.root.visibleIf(filteredPool.isEmpty())
                                        emptyView.emptyTitle.text = "No pool available for $${wallet.symbol}"
                                        emptyView.emptySubtitle.text = "Unfortunately, pools other than ${wallet.pool().value} for $${wallet.symbol} are not available yet. If the pool you are using is not registered yet, you can submit a pool request at support@poolguard.eagercodes.com"

                                        recyclerView.setFlex(RowPool(filteredPool.sortedBy { it.value.lowercase() }
                                            .filter { it.isListed }
                                            .filter { it.poolStatus == PoolStatus.ACTIVE },
                                            object : OnClick<Pool> {
                                                override fun onItemClick(
                                                    item: Pool,
                                                    position: Int
                                                ) {
                                                    try {
                                                        wallet.pool = item.value
                                                        sqliteZUpdate(Wallet::class.java, wallet)
                                                        toasty(Toast.SUCCESS, R.string.settingSaved)
                                                        setResult(Constant.DefaultValue.RESULT_MIGRATE_WALLET)
                                                        bottomSheetMigrate.dismiss()
                                                        recreate()
                                                    } catch (e: Exception) {
                                                        //TODO Raw String
                                                        toasty(Toast.ERROR, "An error occured")
                                                    }
                                                }
                                            },
                                            object : OnFilter<Pool> {
                                                override fun onFilteredResult(list: List<Pool>) {

                                                }
                                            }, false), true)
                                        if(filteredPool.isNotEmpty()) {
                                            recyclerView.setPadding(resources.getDimensionPixelOffset(com.acxdev.commonFunction.R.dimen.x16dp),
                                                resources.getDimensionPixelOffset(com.acxdev.commonFunction.R.dimen.x20dp),
                                                resources.getDimensionPixelOffset(com.acxdev.commonFunction.R.dimen.x16dp),
                                                resources.getDimensionPixelOffset(com.acxdev.commonFunction.R.dimen.x100dp)
                                            )
                                        }
                                    }
                                    showSheetWithExtra(bottomSheetMigrate)
                                }
                                2 -> {
                                    val clipboard =
                                        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("label", wallet.address)

                                    clipboard.setPrimaryClip(clip)
                                    toasty(Toast.SUCCESS, getString(R.string.addressCopiedSuccessfully))
                                }
                                3 -> showSheetWithExtra(SheetDeleteWallet(object :
                                    SheetDeleteWallet.OnSheetDeleteWallet {
                                    override fun onComplete(id: Long) {
                                        setResult(Constant.DefaultValue.RESULT_WALLET_DELETED)
                                        toasty(Toast.SUCCESS, R.string.walletSuccessfullyDeleted)
                                        if (getSavedPrefs(Constant.SharedPrefs.SCREEN_DEFAULT).toString()
                                                .toLong() == id && getSavedPrefs(Constant.SharedPrefs.IS_WALLET_SCREEN).toString()
                                                .toBoolean()
                                        ) {
                                            putPrefs(Constant.SharedPrefs.IS_WALLET_SCREEN, false)
                                        }
                                        onBackPressed()
                                    }
                                }), Gson().toJson(wallet))
                            }
                            menuDashboard.dismiss()
                        }
                    }, true))
            }

            initUI()
            initMain()
            initObserve()
            Pool.WOOLYPOOLYSOLO

            runnable = object : Runnable {
                override fun run() {
                    if(second > 0) {
                        second -= 1
                        autoCheck.text = getString(R.string.updateIn, second)
                        handler.postDelayed(this, Constant.DefaultValue.DELAY_UPDATE_VALUE)
                    } else{
                        initMain()
                        second = Constant.DefaultValue.DURATION_AUTO_CHECK
                        runnable.run()
                    }
                }
            }

            autoCheck.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    runnable.run()
                } else {
                    autoCheck.text = getString(R.string.autoUpdate)
                    handler.removeCallbacksAndMessages(null)
                }
            }
        } catch (e: Exception) {
            toolbar.set(emptyString())
            catchWallet()
        }
    }

    override fun ActivityDashboardBinding.onClickListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        scopeLayout {
            sliderHashRate.sliderView.stopAutoCycle()
            sliderShares.sliderView.stopAutoCycle()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isTaskRoot) {
            Intent(this, ActivityMain::class.java)
                .putExtra(Constant.Intent.CURRENT_POSITION, 1)
                .also { intent ->
                    startActivity(intent)
                    finish()
                }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}