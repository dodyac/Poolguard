package com.acxdev.poolguardapps.ui.worker

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.toZero
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowWorkerBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.util.invalidateFormat
import java.text.DecimalFormat

class RowWorker(
    private val list: MutableList<WorkerList>,
    private val isViewAll: Boolean,
    private val wallet: Wallet,
    private val onClick: OnClick<WorkerList>
) :
    BaseAdapterLib<RowWorkerBinding, WorkerList>(RowWorkerBinding::inflate, list) {

    override fun ViewHolder<RowWorkerBinding>.bind(item: WorkerList) {
        scopeLayout {
            name.text = item.name.toZero()
            averageHashrate.text = item.average.invalidateFormat(wallet.symbol)
            currentHashRate.text = item.current.invalidateFormat(wallet.symbol)

            when (wallet.pool) {
                Pool.EZIL_ZIL.value, Pool.ZETPOOLPPS.value, Pool.UNMINEABLE.value, Pool.EZIL.value, Pool.RAVENMINER.value, Pool.MYMINERSSOLO.value,
                Pool.MYMINERS.value, Pool.MINEXMR.value, Pool.ETHO.value, Pool.WOOLYPOOLYSOLO.value, Pool.SOLOPOOL.value,
                Pool.TWOMINERSSOLO.value, Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value, Pool.K1POOL.value, Pool.LUCKPOOL.value, Pool.TWOMINERS.value, Pool.ZETPOOL.value,
                Pool.CRAZYPOOL.value, Pool.WOOLYPOOLY.value, Pool.NANOPOOL.value  -> validStaleLayout.gone()
                else -> {
                    valid.text = DecimalFormat("0.##").format(item.valid ?: 0)
                    stale.text = DecimalFormat("0.##").format(item.stale ?: 0)
                }
            }

            if (wallet.exception()) {
                layout.setOnClickListener {
                    onClick.onItemClick(item, adapterPosition)
                }
            }
        }
    }

    private fun Wallet.exception(): Boolean {
        return when (pool) {
            Pool.FLOCKPOOL.value, Pool.ZETPOOLPPS.value, Pool.UNMINEABLE.value, Pool.MINERPOOLSOLO.value, Pool.MINERPOOL.value, Pool.HIVEON.value, Pool.RAVENMINER.value,
            Pool.COMINERS.value, Pool.MYMINERSSOLO.value, Pool.MYMINERS.value, Pool.MINEXMR.value, Pool.ETHO.value, Pool.BAIKALMINESOLO.value, Pool.BAIKALMINEPPS.value,
            Pool.BAIKALMINE.value, Pool.WOOLYPOOLYSOLO.value, Pool.SOLOPOOL.value, Pool.TWOMINERSSOLO.value, Pool.K1POOLSOLO.value, Pool.K1POOLRBPPS.value,
            Pool.K1POOL.value, Pool.LUCKPOOL.value, Pool.TWOMINERS.value, Pool.ZETPOOL.value, Pool.CRAZYPOOL.value, Pool.WOOLYPOOLY.value, Pool.HEROMINERS.value -> false
            else -> true
        }
    }

    override fun getItemCount(): Int =
        if (!isViewAll && list.size > Constant.DefaultValue.MAX_WORKERS) {
            Constant.DefaultValue.MAX_WORKERS
        } else {
            list.size
        }
}