package com.acxdev.poolguardapps.service

import android.content.Context
import com.acxdev.commonFunction.common.base.BaseNetworking.Companion.whenLoadedSuccess
import com.acxdev.commonFunction.util.ext.getToday
import com.acxdev.commonFunction.util.ext.toDate
import com.acxdev.commonFunction.util.ext.toDateEpoch
import com.acxdev.commonFunction.util.ext.toZero
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.model.Notification
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
import com.acxdev.poolguardapps.service.NotificationBroadcast.Companion.showNotification
import com.acxdev.poolguardapps.util.CryptoFormat
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.invalidateBaikalMine
import com.acxdev.poolguardapps.util.invalidateK1Pool
import com.acxdev.poolguardapps.util.invalidateMyMiners
import com.acxdev.poolguardapps.util.invalidateSoloPool
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.removeSuffixPool
import com.acxdev.poolguardapps.util.toCrypto
import com.acxdev.poolguardapps.util.toEpochHiveOn
import com.acxdev.poolguardapps.util.toEpochHiveOnChart
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZInsertInto
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZIsRowExist
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import java.util.*

object NotificationChannel {

    fun Context.notificationChannel() {
        val walletList = sqLiteZSelectTable(Wallet::class.java)
        if(walletList.isNotEmpty() && getSavedPrefs(Constant.SharedPrefs.AUTO_CHECK).toString().toBoolean()) {
            val isWorkerCheckActive = getSavedPrefs(Constant.SharedPrefs.WORKER_OFFLINE).toString().toBoolean()
            val isPaymentCheckActive = getSavedPrefs(Constant.SharedPrefs.PAYMENT_NOTIFICATION).toString().toBoolean()
            if(isWorkerCheckActive || isPaymentCheckActive) {
                walletList.forEach { wallet ->
                    when(wallet.pool) {
                        Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value,
                        Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value, Pool.SOLOPOOL.value, Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value,
                        Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value -> {
                            fetchApiOtherPool().minerStatsMain("${wallet.getBaseUrl()}accounts/${wallet.address}").whenLoadedSuccess {
                                try {
                                    if(isWorkerCheckActive) {
                                        workers.toList().forEach {
                                            if(it.second.offline) this@notificationChannel.setWorkerNotification(wallet, it.first)
                                        }
                                    }
                                    if(isPaymentCheckActive) {
                                        payments.forEach {
                                            val invalidateAmount = when(wallet.pool) {
                                                Pool.COMINERS.value -> it.amount * 1000000000.0
                                                Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> it.amount.invalidateMyMiners(wallet.symbol)
                                                Pool.ETHO.value -> it.amount
                                                Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> it.amount.invalidateBaikalMine(wallet.symbol)
                                                Pool.SOLOPOOL.value -> it.amount.invalidateSoloPool(wallet.symbol)
                                                Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> {
                                                    if (wallet.symbol == Crypto.CTXC.name || wallet.symbol == Crypto.ETC.name || wallet.symbol == Crypto.ETHW.name) {
                                                        it.amount * 1000000000.0
                                                    } else {
                                                        it.amount
                                                    }
                                                }
                                                Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> it.amount * 1000000000.0
                                                Pool.CRAZYPOOL.value -> if(wallet.symbol != Crypto.UBQ.name) it.amount * 1000000000.0 else it.amount
                                                else -> 0.0
                                            }
                                            if(it.timestamp.isToday() && isNotificationNotExist(it.tx)) {
                                                this@notificationChannel.setPaymentNotification(wallet, invalidateAmount)
                                                insertNotification(it.tx)
                                            }
                                        }
                                    }
                                } catch (e: Exception){ e.printStackTrace() }
                            }
                        }
                        Pool.FLOCKPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).flockPoolDashboard(wallet.address).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    workers.forEach {
                                        if(it.hashrate.now < 1) this@notificationChannel.setWorkerNotification(wallet, it.name)
                                    }
                                }
                                if(isPaymentCheckActive) {
                                    payments.sortedByDescending { it?.timestamp }.forEach {
                                        it?.let {
                                            if (it.timestamp.isToday() && isNotificationNotExist(it.transaction_id.toString())) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount)
                                                insertNotification(it.transaction_id.toString())
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                        Pool.UNMINEABLE.value -> fetchApiOtherPool(wallet.getBaseUrl()).unMineableStats(wallet.address, wallet.symbol).whenLoadedSuccess {
                            if(isWorkerCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).unMineableWorker(data.uuid).whenLoadedSuccess {
                                    try {
                                        val etHash = data.ethash.workers
                                        val etcHash = data.etchash.workers
                                        val randomX = data.randomx.workers
                                        val kawPow = data.kawpow.workers

                                        etHash.forEach { worker ->
                                            if(worker.chr < 1) this@notificationChannel.setWorkerNotification(wallet, worker.name)
                                        }
                                        etcHash.forEach { worker ->
                                            if(worker.chr < 1) this@notificationChannel.setWorkerNotification(wallet, worker.name)
                                        }
                                        randomX.forEach { worker ->
                                            if(worker.chr < 1) this@notificationChannel.setWorkerNotification(wallet, worker.name)
                                        }
                                        kawPow.forEach { worker ->
                                            if(worker.chr < 1) this@notificationChannel.setWorkerNotification(wallet, worker.name)
                                        }
                                    } catch (e: Exception) { e.printStackTrace() }
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).unMineablePayments(data.uuid).whenLoadedSuccess {
                                    try {
                                        data.list.forEach {
                                            if((it.timestamp / 1000).isToday() && isNotificationNotExist(it.tx)) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                                insertNotification(it.tx)
                                            }
                                        }
                                    } catch (e: Exception) { e.printStackTrace() }
                                }
                            }
                        }
                        //Minerpool Payment is not available yet
                        Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).minerPool(wallet.address, 1).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    workers.toList().forEach {
                                        if(it.second.hashrate < 1) this@notificationChannel.setWorkerNotification(wallet, it.first)
                                    }
                                }
                            } catch (e: Exception){ e.printStackTrace() }
                        }
                        //Ezil worker offline not dont yet
                        Pool.EZIL.value, Pool.EZIL_ZIL.value -> {
                            if(isPaymentCheckActive) {
                                fetchApiEzil(EzilType.BILLING).payout(wallet.address).whenLoadedSuccess {
                                    try {
                                        eth.forEach {
                                            if(it.created_at.toEpochHiveOnChart().isToday() && isNotificationNotExist(it.tx_hash)) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                                insertNotification(it.tx_hash)
                                            }
                                        }
                                        etc.forEach {
                                            if(it.created_at.toEpochHiveOnChart().isToday() && isNotificationNotExist(it.tx_hash)) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                                insertNotification(it.tx_hash)
                                            }
                                        }
                                        zil.forEach {
                                            if(it.created_at.toEpochHiveOnChart().isToday() && isNotificationNotExist(it.tx_hash)) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                                insertNotification(it.tx_hash)
                                            }
                                        }
                                    } catch (e: Exception) { e.printStackTrace() }
                                }
                            }
                        }
                        Pool.HIVEON.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiHiveOn().workers(wallet.address.remove0x(), wallet.symbol).whenLoadedSuccess {
                                    try {
                                        workers.toList().forEach {
                                            if(it.second.online != true) this@notificationChannel.setWorkerNotification(wallet, it.first)
                                        }
                                    } catch (e: Exception){ e.printStackTrace()}
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiHiveOn().payouts(wallet.address.remove0x(), wallet.symbol).whenLoadedSuccess {
                                    try {
                                        items.forEach {
                                            if(it.createdAt.toEpochHiveOn().isToday() && isNotificationNotExist(it.txHash)) {
                                                this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                                insertNotification(it.txHash)
                                            }
                                        }
                                    } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                        }
                        Pool.RAVENMINER.value -> fetchApiOtherPool(wallet.getBaseUrl()).ravenMiner(wallet.address).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    workers.list.forEach {
                                        if(it.online.toString().toBoolean() != true) this@notificationChannel.setWorkerNotification(wallet, it.name)
                                    }
                                }
                                if(isPaymentCheckActive) {
                                    payouts.forEach {
                                        if(it.time.isToday() && isNotificationNotExist(it.tx)){
                                            this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                            insertNotification(it.tx)
                                        }
                                    }
                                }
                            } catch (e: Exception){ e.printStackTrace() }
                        }
                        Pool.MINEXMR.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).mineXmrWorker(wallet.address).whenLoadedSuccess {
                                    try {
                                        forEach {
                                            if(it.hashrate.toZero().toDouble() < 1) this@notificationChannel.setWorkerNotification(wallet, it.name.toZero())
                                        }
                                    } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).mineXmrPayments(wallet.address).whenLoadedSuccess {
                                    try {
                                        payments.forEach {
                                            val split = it.split(Constant.COLON)
                                            val date = split[0].toLong()
                                            val txHash = split[1]
                                            val amount = split[2].toDoubleOrNull()
                                            val fee = split[3]
                                            val idk = split[4]
                                            if (date.isToday() && isNotificationNotExist(txHash)) {
                                                this@notificationChannel.setPaymentNotification(wallet, amount)
                                                insertNotification(txHash)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).k1Pool(wallet.address).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    miner.workers.toList().forEach {
                                        if(it.second.offline) this@notificationChannel.setWorkerNotification(wallet, it.first)
                                    }
                                }
                                if(isPaymentCheckActive) {
                                    miner.payments.forEach {
                                        if(it.timestamp.isToday() && isNotificationNotExist(it.tx)) {
                                            this@notificationChannel.setPaymentNotification(wallet, it.amount.invalidateK1Pool(wallet.symbol)
                                                .toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                            insertNotification(it.tx)
                                        }
                                    }
                                }
                            } catch (e: Exception){ e.printStackTrace()}
                        }
                        Pool.LUCKPOOL.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).luckPool(wallet.address).whenLoadedSuccess {
                                    try {
                                        workers.forEach {
                                            val split = it.split(Constant.COLON)
                                            val name = split[0]
                                            val hashRate = split[1].toDoubleOrNull()
                                            val share = split[2].toDoubleOrNull()
                                            val isOn = split[3]
                                            val tag = split[4]
                                            val isFalse = split[5]

                                            if(isOn == "off" ) this@notificationChannel.setWorkerNotification(wallet, name)
                                        }
                                    } catch (e: Exception){ e.printStackTrace()}
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiOtherPool(wallet.getBaseUrl()).luckPoolPayments(wallet.address).whenLoadedSuccess {
                                    try { forEach {
                                        val split = it.split(Constant.COLON)
                                        val time = split[0].toLong()
                                        val txHash = split[1]
                                        val amount = split[2].toDouble()
                                        val invalidateAmount = if(wallet.symbol == Crypto.ZEC.name) amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble() else amount
                                        if(time.isToday(false) && isNotificationNotExist(txHash)){
                                            this@notificationChannel.setPaymentNotification(wallet, invalidateAmount)
                                            insertNotification(txHash)
                                        }
                                    } } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                        }
                        Pool.WOOLYPOOLY.value, Pool.WOOLYPOOLYSOLO.value -> fetchApiOtherPool(wallet.getBaseUrl()).woolyPooly(wallet.address).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    workers.forEach {
                                        if(it.offline) this@notificationChannel.setWorkerNotification(wallet, it.worker)
                                    }
                                }
                                if(isPaymentCheckActive) {
                                    payments.forEach {
                                        if(it.timestamp.isToday() && isNotificationNotExist(it.tx)){
                                            this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                            insertNotification(it.tx)
                                        }
                                    }
                                }
                            } catch (e: Exception){ e.printStackTrace()}
                        }
                        Pool.HEROMINERS.value -> fetchApiOtherPool(wallet.getBaseUrl()).heroMiners(wallet.address).whenLoadedSuccess {
                            try {
                                if(isWorkerCheckActive) {
                                    workers.forEach {
                                        if(it.hashrate.toZero().toDouble() < 1 && it.lastShare.toZero().toDouble() > 0L)
                                            this@notificationChannel.setWorkerNotification(wallet, it.name)
                                    }
                                }
                                if(isPaymentCheckActive) {
                                    val hash = mutableListOf<String>()
                                    val date = mutableListOf<Long>()
                                    payments.forEachIndexed { index, s ->
                                        if(index %2 != 0) date.add(s.toLong())
                                        else hash.add(s)
                                    }
                                    hash.forEachIndexed { index, s ->
                                        val split = s.split(Constant.COLON)
                                        val txHash = split[0]
                                        val amount = split[1].toDouble()
                                        val fee = split[2]
                                        if(date[index].isToday() && isNotificationNotExist(txHash)) {
                                            this@notificationChannel.setPaymentNotification(wallet, amount)
                                            insertNotification(txHash)
                                        }
                                    }
                                }
                            } catch (e: Exception){ e.printStackTrace()}
                        }
                        Pool.NANOPOOL.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiNanoPool(wallet.getBaseUrl()).stats(wallet.address.removeCfx()).whenLoadedSuccess {
                                    try { data?.workers?.forEach {
                                        if(it.hashrate.toZero().toDouble() < 1)
                                            this@notificationChannel.setWorkerNotification(wallet, it.id)
                                    } } catch (e: Exception){ e.printStackTrace()}
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiNanoPool(wallet.getBaseUrl()).payments(wallet.address.removeCfx()).whenLoadedSuccess {
                                    try { data.forEach {
                                        if(it.date.isToday() && isNotificationNotExist(it.txHash)) {
                                            this@notificationChannel.setPaymentNotification(wallet, it.amount.toCrypto(wallet.symbol, CryptoFormat.MULTIPLY_BLOCK).toDouble())
                                            insertNotification(it.txHash)
                                        }
                                    } } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                        }
                        Pool.FLEXPOOL.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiFlexPool().workers(wallet.symbol, wallet.address).whenLoadedSuccess {
                                    try { result.forEach {
                                        if(!it.isOnline) this@notificationChannel.setWorkerNotification(wallet, it.name)
                                    } } catch (e: Exception){ e.printStackTrace()}
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiFlexPool().payments(wallet.symbol, wallet.address,0).whenLoadedSuccess {
                                    try { result.data.forEach {
                                        if(it.timestamp.isToday() && isNotificationNotExist(it.hash)){
                                            this@notificationChannel.setPaymentNotification(wallet, it.value)
                                            insertNotification(it.hash)
                                        }
                                    } } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                        }
                        Pool.ETHERMINE.value, Pool.FLYPOOL.value -> {
                            if(isWorkerCheckActive) {
                                fetchApiBitFly(wallet.getBaseUrl()).workers(wallet.address).whenLoadedSuccess {
                                    try { data.forEach {
                                        if(it.reportedHashrate.toZero().toDouble() < 1)
                                            this@notificationChannel.setWorkerNotification(wallet, it.worker)
                                    } } catch (e: Exception){ e.printStackTrace()}
                                }
                            }
                            if(isPaymentCheckActive) {
                                fetchApiBitFly(wallet.getBaseUrl()).payout(wallet.address).whenLoadedSuccess {
                                    try { data.forEach {
                                        val tx = it.txHash ?: it.kernelId!!
                                        if(it.paidOn.isToday() && isNotificationNotExist(tx)){
                                            this@notificationChannel.setPaymentNotification(wallet, it.amount)
                                            insertNotification(tx)
                                        }
                                    } } catch (e: Exception){ e.printStackTrace() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Context.setWorkerNotification(
        wallet: Wallet,
        workerName: String?
    ) {
        showNotification(
            Constant.CHANNEL_ID_WORKER_OFFLINE,
            Constant.SharedPrefs.WORKER_OFFLINE,
            wallet.name.ifEmpty { wallet.address },
            getString(
                R.string.isCurrentlyOffline,
                workerName
            )
        )
    }

    private fun Context.setPaymentNotification(
        wallet: Wallet,
        amount: Double?
    ) {
        showNotification(
            Constant.CHANNEL_ID_PAYMENT_NOTIFICATION,
            Constant.SharedPrefs.PAYMENT_NOTIFICATION,
            wallet.name.ifEmpty { wallet.address },
            getString(R.string.hasSent, wallet.pool.removeSuffixPool(),
                amount.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL)
            )
        )
    }

    private fun Long.isToday(isEpoch: Boolean = true): Boolean {
        return if(isEpoch) {
            toDateEpoch(Constant.PATTERN_DATE, Locale.ENGLISH)
        } else {
            toDate(Constant.PATTERN_DATE, Locale.ENGLISH)
        } == getToday(Constant.PATTERN_DATE, Locale.ENGLISH)
    }

    private fun Context.isNotificationNotExist(tx: String): Boolean {
        return !sqLiteZIsRowExist(Notification::class.java, "tx", tx)
    }

    private fun Context.insertNotification(tx: String) {
        sqLiteZInsertInto(Notification::class.java, Notification(tx))
    }
}