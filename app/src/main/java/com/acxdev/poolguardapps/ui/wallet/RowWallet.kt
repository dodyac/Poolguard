package com.acxdev.poolguardapps.ui.wallet

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.util.Diff
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowWalletBinding
import com.acxdev.poolguardapps.model.Raw
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.rest.EzilType
import com.acxdev.poolguardapps.rest.fetchApiBitFly
import com.acxdev.poolguardapps.rest.fetchApiEzil
import com.acxdev.poolguardapps.rest.fetchApiFlexPool
import com.acxdev.poolguardapps.rest.fetchApiHiveOn
import com.acxdev.poolguardapps.rest.fetchApiNanoPool
import com.acxdev.poolguardapps.rest.fetchApiOtherPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.util.CryptoFormat
import com.acxdev.poolguardapps.util.calculateLocalCurrency
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.util.fixHashRate
import com.acxdev.poolguardapps.util.fixHashRateUnMineable
import com.acxdev.poolguardapps.util.formatReadable
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.invalidateBaikalMine
import com.acxdev.poolguardapps.util.invalidateFormat
import com.acxdev.poolguardapps.util.invalidateMyMiners
import com.acxdev.poolguardapps.util.invalidateSoloPool
import com.acxdev.poolguardapps.util.multiplyLocalCurrency
import com.acxdev.poolguardapps.util.pool
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toLocalCurrency
import com.acxdev.poolguardapps.util.zeroLocalCurrency
import java.math.BigDecimal

class RowWallet(private val list: MutableList<Wallet>, private val onCalculated: OnCalculated)
    : BaseAdapterLib<RowWalletBinding,  Wallet>(RowWalletBinding::inflate, list) {

    val calculateHash = mutableListOf<Float>()
    val calculateBalance = mutableListOf<BigDecimal>()
    var price: Map<String, Map<String, Raw>> = mapOf()

    override fun ViewHolder<RowWalletBinding>.bind(item: Wallet) {
        scopeLayout {
            coinIcon.imageUrl(item.symbol.crypto().icon)
            name.text = item.name
            address.text = item.address
            pool.imageUrl(item.pool().icon)

            when(item.pool) {
                Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                    fetchApiEzil(EzilType.BILLING).balance(item.address).onLoaded(context) {
                        unpaidBalance.localCurrency(item.symbol, eth)
                    }
                    fetchApiEzil(EzilType.STATS).currentStats(item.address).onLoaded(context) {
                        val current = if(item.symbol == Crypto.ETHW.name) eth.current_hashrate else etc.current_hashrate
                        hashRate.setHashRate(item.symbol, current)
                    }
                }
                Pool.HIVEON.value -> {
                    fetchApiHiveOn().billing(item.address.remove0x(), item.symbol).onLoaded(context) {
                        unpaidBalance.localCurrency(item.symbol, totalUnpaid)
                    }
                    fetchApiHiveOn().overview(item.address.remove0x(), item.symbol).onLoaded(context) {
                        hashRate.setHashRate(item.symbol, hashrate)
                    }
                }
                Pool.NANOPOOL.value -> fetchApiNanoPool(item.getBaseUrl()).stats(item.address.removeCfx()).onLoaded(context) {
                    unpaidBalance.localCurrency(item.symbol, data?.balance)
                    hashRate.setHashRate(item.symbol, data?.hashrate.fixHashRate(item.symbol))
                }
                Pool.FLEXPOOL.value -> {
                    fetchApiFlexPool().balance(item.symbol, item.address).onLoaded(context) {
                        unpaidBalance.localCurrency(item.symbol, result.balance.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                    }
                    fetchApiFlexPool().stats(item.symbol, item.address).onLoaded(context) {
                        hashRate.setHashRate(item.symbol, result.currentEffectiveHashrate)
                    }
                }
                Pool.ETHERMINE.value, Pool.FLYPOOL.value -> fetchApiBitFly(item.getBaseUrl()).currentStats(item.address).onLoaded(context) {
                    unpaidBalance.localCurrency(item.symbol, data?.unpaid.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                    hashRate.setHashRate(item.symbol, data?.currentHashrate)
                }
                else -> {
                    when(item.pool) {
                        Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value,
                        Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value, Pool.SOLOPOOL.value, Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value,
                        Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value -> {
                            fetchApiOtherPool().minerStatsMain("${item.getBaseUrl()}accounts/${item.address}").onLoaded(context) {
                                val invalidateBalance = when(item.pool) {
                                    Pool.COMINERS.value -> stats.balance * 1000000000.0
                                    Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> stats.balance.invalidateMyMiners(item.symbol)
                                    Pool.ETHO.value -> stats.balance
                                    Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> stats.balance.invalidateBaikalMine(item.symbol)
                                    Pool.SOLOPOOL.value -> stats.balance.invalidateSoloPool(item.symbol)
                                    Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                                        if(item.symbol == Crypto.CTXC.name || item.symbol == Crypto.ETC.name || item.symbol == Crypto.ETHW.name){
                                            stats.balance * 1000000000.0
                                        } else {
                                            stats.balance
                                        }
                                    }
                                    Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> stats.balance * 1000000000.0
                                    Pool.CRAZYPOOL.value -> if(item.symbol != Crypto.UBQ.name) stats.balance * 1000000000.0 else stats.balance
                                    else -> 0.0
                                }
                                unpaidBalance.localCurrency(item.symbol, invalidateBalance.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                                hashRate.setHashRate(item.symbol, currentHashrate)
                            }
                        } else -> {
                            val fetchApiOtherPool = fetchApiOtherPool(item.getBaseUrl())
                            when(item.pool) {
                                Pool.RAVENPOOL.value -> fetchApiOtherPool.ravenPool(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, balance)
                                    hashRate.setHashRate(item.symbol, totalHash)
                                }
                                Pool.FLOCKPOOL.value -> fetchApiOtherPool.flockPoolDashboard(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, balance.mature.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                                    hashRate.setHashRate(item.symbol, workers.map { it.hashrate.now }.sum())
                                }
                                Pool.UNMINEABLE.value -> fetchApiOtherPool.unMineableStats(item.address, item.symbol).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, data.balance)
                                    fetchApiOtherPool.unMineableWorker(data.uuid).onLoaded(context) {
                                        val etHash = data.ethash.workers.sumOf { it.chr.toDouble() }
                                        val etcHash = data.etchash.workers.sumOf { it.chr.toDouble() }
                                        val randomX = data.randomx.workers.sumOf { it.chr.toDouble() }
                                        val kawPow = data.kawpow.workers.sumOf { it.chr.toDouble() }

                                        val totalHash = etHash + etcHash + randomX + kawPow

                                        hashRate.setHashRate(item.symbol, totalHash.fixHashRateUnMineable(item.symbol))
                                    }
                                }
                                Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> fetchApiOtherPool.minerPool(item.address, 1).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, balance)
                                    hashRate.setHashRate(item.symbol, hashAvg5m)
                                }
                                Pool.RAVENMINER.value -> fetchApiOtherPool.ravenMiner(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, balance.pending)
                                    hashRate.setHashRate(item.symbol, hashrate.the5Min)
                                }
                                Pool.MINEXMR.value -> {
                                    fetchApiOtherPool.mineXmrBalance(item.address).onLoaded(context) {
                                        unpaidBalance.localCurrency(item.symbol, balance.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                                    }
                                    fetchApiOtherPool.mineXmrWorker(item.address).onLoaded(context) {
                                        val hashrate = sumOf { it.hashrate.toDouble() }.toFloat()
                                        hashRate.setHashRate(item.symbol, hashrate)
                                    }
                                }
                                Pool.WOOLYPOOLYSOLO.value -> fetchApiOtherPool.woolyPooly(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, stats.balance)
                                    hashRate.setHashRate(item.symbol, mode_stats.solo.default.hashrate)
                                }
                                Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> fetchApiOtherPool.k1Pool(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, miner.pendingBalance)
                                    hashRate.setHashRate(item.symbol, miner.curHashrate)
                                }
                                Pool.LUCKPOOL.value -> fetchApiOtherPool.luckPool(item.address).onLoaded(context) {
                                    val invalidateAmount = if(item.symbol == Crypto.ZEC.name) balance.toCrypto(item.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else balance
                                    unpaidBalance.localCurrency(item.symbol, invalidateAmount)
                                    hashRate.setHashRate(item.symbol, hashrateSols)
                                }
                                Pool.WOOLYPOOLY.value -> fetchApiOtherPool.woolyPooly(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, stats.balance)
                                    hashRate.setHashRate(item.symbol, mode_stats.pplns.default.hashrate)
                                }
                                Pool.HEROMINERS.value -> fetchApiOtherPool.heroMiners(item.address).onLoaded(context) {
                                    unpaidBalance.localCurrency(item.symbol, stats.balance.toCrypto(item.symbol, CryptoFormat.DIVIDE_BLOCK))
                                    hashRate.setHashRate(item.symbol, stats.hashrate)
                                }
                            }
                        }
                    }
                }
            }

            card.setOnClickListener {
                onCalculated.onItemClick(item, adapterPosition)
            }
        }
    }

    fun updateWith(newList: List<Wallet>) {
        val diff = Diff(list, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        calculateHash.clear()
        calculateBalance.clear()
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun TextView.localCurrency(symbol: String, unpaid: Any?) {
        val currentSymbol = context.getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()
        try {
            val thePrice = price[symbol]!![currentSymbol]!!
            text = if (context.getSavedPrefs(Constant.SharedPrefs.IS_FIAT).toString().toBoolean()) {
                try {
                    try {
                        unpaid.toString().toBigDecimal().calculateLocalCurrency(thePrice.PRICE, context)
                    } catch (e: Exception) {
                        context.zeroLocalCurrency()
                    }
                } catch (e: Exception) {
                    unpaid.toCrypto(symbol, CryptoFormat.FORMAT_SYMBOL)
                }
            } else {
                unpaid.toCrypto(symbol, CryptoFormat.FORMAT_SYMBOL)
            }
            unpaid?.multiplyLocalCurrency(thePrice.PRICE)?.let { calculateBalance.add(it) }
        } catch (e: Exception) {
            unpaid.toCrypto(symbol, CryptoFormat.FORMAT_SYMBOL)
        }
        onCalculated.calculatedBalance(calculateBalance.sumOf { it }.toLocalCurrency(context))
    }

    private fun TextView.setHashRate(symbol: String, hashRate: Float?) {
        text = hashRate.invalidateFormat(symbol)
        if(symbol != Crypto.XCH.name) {
            hashRate?.let { calculateHash.add(it) }
            onCalculated.calculatedHash(calculateHash.sumOf { it.toDouble() }.formatReadable())
        }
    }

    interface OnCalculated {
        fun calculatedHash(totalHash: String)
        fun calculatedBalance(totalBalance: String)
        fun onItemClick(item: Wallet, position: Int)
    }
}