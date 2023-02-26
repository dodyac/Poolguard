package com.acxdev.poolguardapps.ui.worker

import com.acxdev.commonFunction.util.ext.view.setGrid
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityWorkersBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.rest.EzilType
import com.acxdev.poolguardapps.rest.fetchApiBitFly
import com.acxdev.poolguardapps.rest.fetchApiEzil
import com.acxdev.poolguardapps.rest.fetchApiFlexPool
import com.acxdev.poolguardapps.rest.fetchApiHiveOn
import com.acxdev.poolguardapps.rest.fetchApiNanoPool
import com.acxdev.poolguardapps.rest.fetchApiOtherPool
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.util.fixHashRate
import com.acxdev.poolguardapps.util.fixHashRateUnMineable
import com.acxdev.poolguardapps.util.getBaseUrl
import com.acxdev.poolguardapps.util.remove0x
import com.acxdev.poolguardapps.util.removeCfx
import com.acxdev.poolguardapps.util.removePrefixAddress
import com.acxdev.poolguardapps.util.workerOnClick
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZGetById

class ActivityWorkers : BaseActivity<ActivityWorkersBinding>(ActivityWorkersBinding::inflate) {

    private lateinit var wallet: Wallet
    private val workersList= mutableListOf<WorkerList>()

    override fun ActivityWorkersBinding.configureViews() {
        toolbar.set(R.string.workers)

        wallet = sqLiteZGetById(Wallet::class.java, intent.getStringExtra(Constant.Intent.ID)!!.toLong())

        when(wallet.pool) {
            Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value,
            Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value, Pool.SOLOPOOL.value, Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value,
            Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value -> {
                fetchApiOtherPool().minerStatsMain("${wallet.getBaseUrl()}accounts/${wallet.address}").onLoaded(this@ActivityWorkers) {
                    workers.toList().forEach {
                        when(wallet.pool) {
                            Pool.COMINERS.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2, it.second.v_per, it.second.i_per))
                            Pool.MYMINERSSOLO.value, Pool.MYMINERS.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                            Pool.ETHO.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                            Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value, Pool.BAIKALMINE.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2, it.second.A, it.second.S))
                            Pool.SOLOPOOL.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                            Pool.TWOMINERS.value, Pool.TWOMINERSSOLO.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                            Pool.ZETPOOLPPS.value, Pool.ZETPOOL.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                            Pool.CRAZYPOOL.value -> workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                        }
                    }
                    initWorkers()
                }
            }
            Pool.FLOCKPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).flockPoolDashboard(wallet.address).onLoaded(this@ActivityWorkers) {
                try {
                    workers.forEach {
                        workersList.add(
                            WorkerList(it.name, it.hashrate.now, it.hashrate.avg,
                            it.shares.accepted.toFloat(), it.shares.stale.toFloat())
                        )
                    }
                    initWorkers()
                } catch (e: Exception) { e.printStackTrace() }
            }
            Pool.UNMINEABLE.value -> fetchApiOtherPool(wallet.getBaseUrl()).unMineableStats(wallet.address, wallet.symbol).onLoaded(this@ActivityWorkers) {
                try {
                    fetchApiOtherPool(wallet.getBaseUrl()).unMineableWorker(data.uuid).onLoaded(this@ActivityWorkers) {
                        try {
                            val etHash = data.ethash.workers
                            val etcHash = data.etchash.workers
                            val randomX = data.randomx.workers
                            val kawpow = data.kawpow.workers

                            etHash.forEach {
                                workersList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                            }
                            etcHash.forEach {
                                workersList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                            }
                            randomX.forEach {
                                workersList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                            }
                            kawpow.forEach {
                                workersList.add(WorkerList(it.name, it.chr.fixHashRateUnMineable(wallet.symbol), it.rhr.fixHashRateUnMineable(wallet.symbol)))
                            }
                            initWorkers()
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).minerPool(wallet.address, 1).onLoaded(this@ActivityWorkers) {
                workers.toList().forEach {
                    workersList.add(
                        WorkerList(it.first.removePrefixAddress(wallet), it.second.hashrate.toFloat(), it.second.hashrate.toFloat(),
                            it.second.shares.toFloat(), it.second.invalidshares.toFloat())
                    )
                }
                initWorkers()
            }
            Pool.EZIL.value, Pool.EZIL_ZIL.value -> fetchApiEzil(EzilType.STATS).worker(wallet.address).onLoaded(this@ActivityWorkers) {
                forEach {
                    workersList.add(WorkerList(it.worker, it.current_hashrate, it.average_hashrate))
                }
                initWorkers()
            }
            Pool.HIVEON.value -> fetchApiHiveOn().workers(wallet.address.remove0x(), wallet.symbol).onLoaded(this@ActivityWorkers) {
                workers.toList().forEach {
                    workersList.add(
                        WorkerList(it.first, it.second.hashrate, it.second.hashrate24h,
                            it.second.sharesStatusStats.validCount, it.second.sharesStatusStats.staleCount)
                    )
                }
                initWorkers()
            }
            Pool.RAVENMINER.value -> fetchApiOtherPool(wallet.getBaseUrl()).ravenMiner(wallet.address).onLoaded(this@ActivityWorkers) {
                workers.list.forEach {
                    workersList.add(WorkerList(it.name, it.hr5m, it.hr5m))
                }
                initWorkers()
            }
            Pool.MINEXMR.value -> fetchApiOtherPool(wallet.getBaseUrl()).mineXmrWorker(wallet.address).onLoaded(this@ActivityWorkers) {
                forEach {
                    workersList.add(WorkerList(it.name, it.hashrate, it.hashrate))
                }
                initWorkers()
            }
            Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).k1Pool(wallet.address).onLoaded(this@ActivityWorkers) {
                miner.workers.toList().forEach {
                    workersList.add(WorkerList(it.first, it.second.hr, it.second.hr2))
                }
                initWorkers()
            }
            Pool.LUCKPOOL.value -> fetchApiOtherPool(wallet.getBaseUrl()).luckPool(wallet.address).onLoaded(this@ActivityWorkers) {
                workers.forEach {
                    val split = it.split(Constant.COLON)
                    val name = split[0]
                    val hashRate = split[1].toFloatOrNull()
                    val share = split[2].toFloatOrNull()
                    val isOn = split[3]
                    val tag = split[4]
                    val isFalse = split[5]

                    workersList.add(WorkerList(name, hashRate, hashRate))
                }
                initWorkers()
            }
            Pool.WOOLYPOOLY.value, Pool.WOOLYPOOLYSOLO.value -> fetchApiOtherPool(wallet.getBaseUrl()).woolyPooly(wallet.address).onLoaded(this@ActivityWorkers) {
                workers.forEach {
                    workersList.add(WorkerList(it.worker, it.hr, it.hr3))
                }
                initWorkers()
            }
            Pool.HEROMINERS.value -> fetchApiOtherPool(wallet.getBaseUrl()).heroMiners(wallet.address).onLoaded(this@ActivityWorkers) {
                workers.forEach {
                    workersList.add(
                        WorkerList(it.name, it.hashrate, it.hashrate_24h,
                            it.shares_good, it.shares_stale)
                    )
                }
                initWorkers()
            }
            Pool.NANOPOOL.value -> fetchApiNanoPool(wallet.getBaseUrl()).stats(wallet.address.removeCfx()).onLoaded(this@ActivityWorkers) {
                data?.workers?.forEach {
                    workersList.add(
                        WorkerList(it.id, it.hashrate.fixHashRate(wallet.symbol),
                            it.h24.fixHashRate(wallet.symbol))
                    )
                }
                initWorkers()
            }
            Pool.FLEXPOOL.value -> fetchApiFlexPool().workers(wallet.symbol, wallet.address).onLoaded(this@ActivityWorkers) {
                result.forEach {
                    workersList.add(
                        WorkerList(it.name, it.currentEffectiveHashrate,
                            it.averageEffectiveHashrate, it.validShares, it.staleShares)
                    )
                }
                initWorkers()
            }
            Pool.ETHERMINE.value, Pool.FLYPOOL.value -> fetchApiBitFly(wallet.getBaseUrl()).workers(wallet.address).onLoaded(this@ActivityWorkers) {
                data.forEach {
                    workersList.add(
                        WorkerList(it.worker, it.currentHashrate, it.averageHashrate,
                        it.validShares, it.staleShares)
                    )
                }
                initWorkers()
            }
        }
    }

    override fun ActivityWorkersBinding.onClickListener() {

    }

    private fun initWorkers() {
        scopeLayout {
            toolbar.set(getString(R.string.activeWorkersNum, workersList.size))
            val adapter = RowWorker(workersList,true, wallet, workerOnClick(wallet))
            workers.setGrid(adapter,200F)
        }
    }
}