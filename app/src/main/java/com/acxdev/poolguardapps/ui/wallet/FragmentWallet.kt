package com.acxdev.poolguardapps.ui.wallet

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.FragmentWalletBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.dashboard.ActivityDashboard
import com.acxdev.poolguardapps.ui.pool.ActivityPool
import com.acxdev.poolguardapps.util.balloonMenu
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable

class FragmentWallet : BaseFragment<FragmentWalletBinding>(FragmentWalletBinding::inflate) {

    private lateinit var adapter: RowWallet
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            safeContext {
                when (it.resultCode) {
                    Constant.DefaultValue.RESULT_WALLET_ADDED -> {
                        val walletList = sqLiteZSelectTable(Wallet::class.java)
                        adapter.updateWith(walletList)
                        initFilledWallet(walletList)
                    }
                    Constant.DefaultValue.RESULT_MIGRATE_WALLET -> {
                        val walletList = sqLiteZSelectTable(Wallet::class.java)
                        adapter.updateWith(walletList)
                        initFilledWallet(walletList)
                    }
                    Constant.DefaultValue.RESULT_WALLET_DELETED -> {
                        val walletList = sqLiteZSelectTable(Wallet::class.java)
                        adapter.updateWith(walletList)
                        scopeLayout {
                            textView3.text = if(walletList.size > 1) getString(R.string.wallets) else getString(
                                R.string.wallet)
                        }
                        if (walletList.isEmpty()) initEmptyWallet()
                    }
                }
            }
        }

    var second = Constant.DefaultValue.DURATION_AUTO_CHECK

    override fun FragmentWalletBinding.configureViews() {
        safeContext {
            balance.text.text = getString(R.string.totalBalance)
            hashRate.text.text = getString(R.string.totalHashrate)
            button.visible()
            showBy.setImageResource(if(prefs(Constant.SharedPrefs.IS_FIAT).toBoolean()) R.drawable.icons8_money else R.drawable.icons8_stack_of_coins)
            handler = Handler(Looper.getMainLooper())

            adapter = RowWallet(mutableListOf(), object : RowWallet.OnCalculated {
                override fun calculatedHash(totalHash: String) {
                    scopeLayout {
                        hashRate.value.text = totalHash
                    }
                }

                override fun calculatedBalance(totalBalance: String) {
                    scopeLayout {
                        balance.value.text = totalBalance
                    }
                }

                override fun onItemClick(item: Wallet, position: Int) {
                    resultLauncher.launch(
                        Intent(context, ActivityDashboard::class.java)
                            .putExtra(Constant.Intent.ID, item._id.toString())
                    )
                }
            })
            wallet.setVStack(adapter, hasFixed = true)

            init()

            object : Runnable {
                override fun run() {
                    if(second > 0) {
                        second -= 1
                        autoCheck.text = getString(R.string.updateIn, second)
                        handler.postDelayed(this, Constant.DefaultValue.DELAY_UPDATE_VALUE)
                    } else {
                        init()
                        second = Constant.DefaultValue.DURATION_AUTO_CHECK
                        runnable.run()
                    }
                }
            }.also { runnable = it }
        }
    }

    override fun FragmentWalletBinding.onClickListener() {
        safeContext {
            button.setOnClickListener {
                resultLauncher.launch(Intent(context, ActivityPool::class.java))
            }
            addWallet.setOnClickListener {
                Intent(this, ActivityPool::class.java)
                    .also {
                        resultLauncher.launch(it)
                    }
            }
            autoCheck.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    runnable.run()
                } else {
                    autoCheck.text = getString(R.string.autoUpdate)
                    handler.removeCallbacksAndMessages(null)
                }
            }
            sort.setOnClickListener {
                val menuSort = balloonMenu(it)
                val list = mutableListOf<String>()
                list.add(getString(R.string.sortByName))
                list.add(getString(R.string.sortByPool))
                list.add(getString(R.string.sortByAddress))
                list.add(getString(R.string.sortByCoin))
                menuSort.getContentView().findViewById<RecyclerView>(R.id.recycler)
                    .setVStack(RowMenu(list, object : OnClick<String> {
                        override fun onItemClick(item: String, position: Int) {
                            val newList = sqLiteZSelectTable(Wallet::class.java)
                            val sortBy = newList.sortedBy { wallet ->
                                when (position) {
                                    0 -> wallet.name.lowercase()
                                    1 -> wallet.pool.lowercase()
                                    2 -> wallet.address.lowercase()
                                    else -> wallet.symbol.lowercase()
                                }
                            }
                            adapter.updateWith(sortBy)
                            menuSort.dismiss()
                        }
                    }, false), hasFixed = true)
            }
            showBy.setOnClickListener {
                adapter.calculateHash.clear()
                adapter.calculateBalance.clear()
                if (prefs(Constant.SharedPrefs.IS_FIAT).toBoolean()) {
                    showBy.setImageResource(R.drawable.icons8_stack_of_coins)
                    putPrefs(Constant.SharedPrefs.IS_FIAT, false)
                } else {
                    showBy.setImageResource(R.drawable.icons8_money)
                    putPrefs(Constant.SharedPrefs.IS_FIAT, true)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun init() {
        scopeLayout {
            safeContext {
                val walletList = sqLiteZSelectTable(Wallet::class.java)
                if (walletList.isEmpty()) initEmptyWallet()
                else {
                    initFilledWallet(walletList)
                    wallet.showShimmer()
                    val cryptoNameList = walletList.distinctBy { it.symbol }.joinToString { it.symbol }.replace(" ", "")
                    fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getPriceMultiFull(cryptoNameList, currency).onLoaded(this) {
                        wallet.hideShimmer()
                        adapter.price = RAW
                        adapter.updateWith(walletList)
                    }
                }
            }
        }
    }

    private fun initFilledWallet(walletList: List<Wallet>) {
        scopeLayout {
            empty.gone()
            layout.visible()
            lottie.cancelAnimation()

            textView3.text = if(walletList.size > 1) getString(R.string.wallets) else getString(R.string.wallet)
        }
    }

    private fun initEmptyWallet() {
        scopeLayout {
            empty.visible()
            layout.gone()
        }
    }
}