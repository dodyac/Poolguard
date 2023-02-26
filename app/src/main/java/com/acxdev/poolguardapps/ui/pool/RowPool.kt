package com.acxdev.poolguardapps.ui.pool

import com.acxdev.commonFunction.adapter.BaseAdapterFilterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowPoolBinding
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.repository.PoolStatus
import com.acxdev.poolguardapps.util.imageUrl

class RowPool(list: List<Pool>, private val onClick: OnClick<Pool>, onFilter: OnFilter<Pool>, private val isCoinCountVisible: Boolean = true)
    : BaseAdapterFilterLib<RowPoolBinding, Pool> (RowPoolBinding::inflate, list, onFilter) {

    override fun ViewHolder<RowPoolBinding>.bind(item: Pool) {
        scopeLayout {
            poolIcon.imageUrl(item.icon)
            poolName.text = item.value
            poolDomain.text = item.domain
            when (item.poolStatus) {
                PoolStatus.ACTIVE -> {
                    soon.apply {
                        visibleIf(isCoinCountVisible)
                        if(isCoinCountVisible) {
                            val size = item.coin.size
                            text = if (size < 2) context.getString(R.string.totalCoin, size) else context.getString(R.string.totalCoins, size)
                            backgroundTint(context.getColor(R.color.greenSecondary))
                            setTextColor(context.getColor(R.color.green))
                        }
                    }
                    pool.isEnabled = true

                    pool.setOnClickListener {
                        onClick.onItemClick(item, adapterPosition)
                    }
                }
                PoolStatus.PROGRESS -> {
                    pool.isEnabled = false
                    soon.apply {
                        text = context.getString(R.string.inProgress)
                        backgroundTint(context.getColor(R.color.blueSecondary))
                        setTextColor(context.getColor(R.color.primaryBlue))
                    }
                }
                PoolStatus.SOON -> {
                    pool.isEnabled = false
                    soon.apply {
                        text = context.getString(R.string.soon)
                        backgroundTint(context.getColor(R.color.yellowSecondary))
                        setTextColor(context.getColor(R.color.yellow))
                    }
                }
            }
        }
    }

    override fun filterBy(item: Pool, query: String): Boolean {
        val poolName = item.value.lowercase()
        val poolDomain = item.domain.lowercase()
        val poolStatus = item.poolStatus.value.lowercase()
        val poolCoinName = item.coin.any { query.contains(it.crypto.fullName.lowercase()) }
        val poolCoinSymbol = item.coin.any { query.contains(it.crypto.name.lowercase()) }
        return poolName.contains(query) || poolDomain.contains(query) || poolStatus.contains(query) || poolCoinName || poolCoinSymbol
    }
}