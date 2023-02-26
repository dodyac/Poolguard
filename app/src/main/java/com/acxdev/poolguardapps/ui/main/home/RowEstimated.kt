package com.acxdev.poolguardapps.ui.main.home

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.util.balloonText
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.databinding.RowEstimatedBinding
import com.acxdev.poolguardapps.model.Estimated
import com.acxdev.poolguardapps.util.imageUrl
import com.skydoves.balloon.ArrowOrientation

class RowEstimated(val list: MutableList<Estimated>) :
    BaseAdapterLib<RowEstimatedBinding, Estimated>(RowEstimatedBinding::inflate, list) {

    override fun ViewHolder<RowEstimatedBinding>.bind(item: Estimated) {
        scopeLayout {
            coinIcon.imageUrl(item.wallet.symbol.crypto().icon)
            name.text = item.wallet.symbol.crypto().fullName
            symbol.text = item.hashRate
            rewardDaily.text = item.rewardDaily
            localDaily.text = item.localDaily
            rewardMonthly.text = item.rewardMonthly
            localMonthly.text = item.localMonthly

            info.setOnClickListener {
                context.balloonText(
                    ArrowOrientation.RIGHT,
                    context.getString(R.string.estimatedEarningsInfo)
                ).showAlignLeft(it)
            }
        }
    }
}