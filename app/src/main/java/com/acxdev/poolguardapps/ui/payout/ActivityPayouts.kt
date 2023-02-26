package com.acxdev.poolguardapps.ui.payout

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityPayoutsBinding
import com.acxdev.poolguardapps.model.PaymentList
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
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.invalidateBaikalMine
import com.acxdev.poolguardapps.util.invalidateK1Pool
import com.acxdev.poolguardapps.util.invalidateMyMiners
import com.acxdev.poolguardapps.util.invalidateSoloPool
import com.acxdev.poolguardapps.util.paymentOnClick
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toEpochHiveOn
import com.acxdev.poolguardapps.util.toEpochHiveOnChart
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZGetById

class ActivityPayouts : BaseActivity<ActivityPayoutsBinding>(ActivityPayoutsBinding::inflate) {

    private lateinit var adapter: RowPayout
    private lateinit var wallet: Wallet
    private val payouts = mutableListOf<PaymentList>()

    override fun ActivityPayoutsBinding.configureViews() {
        toolbar.set(R.string.payments)

        wallet = sqLiteZGetById(Wallet::class.java, intent.getStringExtra(Constant.Intent.ID)!!.toLong())

        when(wallet.pool) {
            Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value,
            Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value, Pool.SOLOPOOL.value, Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value,
            Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value -> {
                fetchApiOtherPool().minerStatsMain("${wallet.getBaseUrl()}accounts/${wallet.address}").onLoaded(this@ActivityPayouts) {
                    payments.forEach {
                        val invalidateAmount =
                            when(wallet.pool) {
                                Pool.COMINERS.value -> it.amount * 1000000000.0
                                Pool.MYMINERS.value, Pool.MYMINERSSOLO.value -> it.amount.invalidateMyMiners(wallet.symbol)
                                Pool.ETHO.value -> it.amount
                                Pool.BAIKALMINEPPS.value, Pool.BAIKALMINESOLO.value, Pool.BAIKALMINE.value -> it.amount.invalidateBaikalMine(wallet.symbol)
                                Pool.SOLOPOOL.value -> it.amount.invalidateSoloPool(wallet.symbol)
                                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                                    if(wallet.symbol == Crypto.CTXC.name || wallet.symbol == Crypto.ETC.name || wallet.symbol == Crypto.ETHW.name) {
                                        it.amount * 1000000000.0
                                    } else {
                                        it.amount
                                    }
                                }
                                Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> it.amount * 1000000000.0
                                Pool.CRAZYPOOL.value -> if(wallet.symbol != Crypto.UBQ.name) it.amount * 1000000000.0 else it.amount
                                else -> 0.0
                            }
                        payouts.add(PaymentList(invalidateAmount, it.tx, wallet.symbol, it.timestamp))
                    }
                    initPayments()
                }
            }
            Pool.FLOCKPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).flockPoolPayments(wallet.address).onLoaded(this@ActivityPayouts) {
                payments.sortedByDescending { it.timestamp }.forEach {
                    payouts.add(PaymentList(it.amount, it.transaction_id, wallet.symbol, it.timestamp))
                }
                initPayments()
            }
            Pool.UNMINEABLE.value -> fetchApiOtherPool(wallet.getBaseUrl()).unMineableStats(wallet.address, wallet.symbol).onLoaded(this@ActivityPayouts) {
                fetchApiOtherPool(wallet.getBaseUrl()).unMineablePayments(data.uuid).onLoaded(this@ActivityPayouts)  {
                    data.list.forEach {
                        payouts.add(PaymentList(it.amount, it.tx, data.coin, it.timestamp / 1000))
                    }
                    initPayments()
                }
            }
            Pool.EZIL.value, Pool.EZIL_ZIL.value -> fetchApiEzil(EzilType.BILLING).payout(wallet.address).onLoaded(this@ActivityPayouts) {
                eth.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.tx_hash, Crypto.ETHW.name, it.created_at.toEpochHiveOnChart()))
                }
                etc.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.tx_hash, Crypto.ETC.name, it.created_at.toEpochHiveOnChart()))
                }
                zil.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.tx_hash, Crypto.ZIL.name, it.created_at.toEpochHiveOnChart()))
                }
                initPayments()
            }
            Pool.HIVEON.value -> fetchApiHiveOn().payouts(wallet.address.remove0x(), wallet.symbol).onLoaded(this@ActivityPayouts) {
                items.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.txHash, wallet.symbol, it.createdAt.toEpochHiveOn()))
                }
                initPayments()
            }
            Pool.RAVENMINER.value -> fetchApiOtherPool(wallet.getBaseUrl()).ravenMiner(wallet.address).onLoaded(this@ActivityPayouts) {
                payouts.forEach {
                    this@ActivityPayouts.payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.tx, wallet.symbol, it.time))
                }
                initPayments()
            }
            Pool.MINEXMR.value -> fetchApiOtherPool(wallet.getBaseUrl()).mineXmrPayments(wallet.address).onLoaded(this@ActivityPayouts) {
                payments.forEach { payments ->
                    val split = payments.split(Constant.COLON)
                    val date = split[0].toLong()
                    val txHash = split[1]
                    val amount = split[2].toDoubleOrNull()
                    val fee = split[3]
                    val idk = split[4]
                    amount?.let { PaymentList(it, txHash, wallet.symbol, date) }?.let { payouts.add(it) }
                }
                initPayments()
            }
            Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).k1Pool(wallet.address).onLoaded(this@ActivityPayouts) {
                miner.payments.forEach {
                    payouts.add(PaymentList(it.amount.invalidateK1Pool(wallet.symbol)
                        .toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(), it.tx, wallet.symbol, it.timestamp))
                }
                initPayments()
            }
            Pool.LUCKPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).luckPoolPayments(wallet.address).onLoaded(this@ActivityPayouts) {
                forEach {
                    val split = it.split(Constant.COLON)
                    val time = split[0].toLong()
                    val txHash = split[1]
                    val amount = split[2].toDouble()
                    val invalidateAmount = if(wallet.symbol == Crypto.ZEC.name) amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else amount

                    payouts.add(PaymentList(invalidateAmount, txHash, wallet.symbol, time))
                }
                initPayments()
            }
            Pool.WOOLYPOOLY.value, Pool.WOOLYPOOLYSOLO.value -> fetchApiOtherPool(wallet.getBaseUrl()).woolyPooly(wallet.address).onLoaded(this@ActivityPayouts) {
                payments.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.tx, wallet.symbol, it.timestamp))
                }
                initPayments()
            }
            Pool.HEROMINERS.value -> fetchApiOtherPool(wallet.getBaseUrl()).heroMiners(wallet.address).onLoaded(this@ActivityPayouts) {
                val hash = mutableListOf<String>()
                val date = mutableListOf<Long>()
                payments.forEachIndexed { index, s ->
                    if(index %2 != 0) date.add(s.toLong()) else hash.add(s)
                }
                hash.forEachIndexed { index, s ->
                    val split = s.split(Constant.COLON)
                    val txHash = split[0]
                    val amount = split[1].toDouble()
                    val fee = split[2]
                    payouts.add(PaymentList(amount, txHash, wallet.symbol, date[index]))
                }
                initPayments()
            }
            Pool.NANOPOOL.value -> fetchApiNanoPool(wallet.getBaseUrl()).payments(wallet.address.removeCfx()).onLoaded(this@ActivityPayouts) {
                data.forEach {
                    payouts.add(PaymentList(it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble(),
                        it.txHash, wallet.symbol, it.date))
                }
                initPayments()
            }
            Pool.FLEXPOOL.value -> fetchApiFlexPool().payments(wallet.symbol, wallet.address,0).onLoaded(this@ActivityPayouts) {
                result.data.forEach {
                    payouts.add(PaymentList(it.value, it.hash, wallet.symbol, it.timestamp))
                }
                initPayments()
                var pageNow = 1L
                payout.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(!recyclerView.canScrollVertically(1)) {
                            if(pageNow < result.totalPages) {
                                fetchApiFlexPool().payments(wallet.symbol, wallet.address, pageNow).onLoaded(this@ActivityPayouts, true) {
                                    result.data.forEach {
                                        payouts.add(PaymentList(it.value, it.hash, wallet.symbol, it.timestamp))
                                    }
                                    adapter.notifyDataSetChanged()
                                    pageNow += 1
                                }
                            }
                        }
                    }
                })
            }
            Pool.ETHERMINE.value, Pool.FLYPOOL.value -> fetchApiBitFly(wallet.getBaseUrl()).payout(wallet.address).onLoaded(this@ActivityPayouts) {
                data.forEach {
                    payouts.add(PaymentList(it.amount, it.txHash, wallet.symbol, it.paidOn, it.kernelId))
                }
                initPayments()
            }
        }
    }

    override fun ActivityPayoutsBinding.onClickListener() {

    }

    private fun initPayments(){
        scopeLayout {
            adapter = RowPayout(payouts,true, wallet, paymentOnClick(wallet))
            payout.setVStack(adapter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}