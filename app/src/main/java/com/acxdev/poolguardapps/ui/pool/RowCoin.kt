package com.acxdev.poolguardapps.ui.pool

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowCoinBinding
import com.acxdev.poolguardapps.model.coin.Coin
import com.acxdev.poolguardapps.util.imageUrl

class RowCoin(list: MutableList<Coin>, private val onClick: OnClick<Coin>)
    : BaseAdapterLib<RowCoinBinding, Coin>(RowCoinBinding::inflate, list) {

    override fun ViewHolder<RowCoinBinding>.bind(item: Coin) {
        scopeLayout {
            coinIcon.imageUrl(item.crypto.icon)
            coinName.text = item.crypto.name

            if (Constant.SELECTED_COIN == adapterPosition) {
                card.setCardBackgroundColor(context.getColorPrimary())
                coinName.setTextColor(context.getColor(com.acxdev.commonFunction.R.color.white))
            } else {
                card.setCardBackgroundColor(context.getColor(R.color.cardBackground))
                coinName.setTextColor(context.getColor(R.color.text))
            }

            card.setOnClickListener {
                if (Constant.SELECTED_COIN >= 0) {
                    notifyItemChanged(Constant.SELECTED_COIN)
                }
                Constant.SELECTED_COIN = adapterPosition
                notifyItemChanged(Constant.SELECTED_COIN)
                onClick.onItemClick(item, adapterPosition)
            }
        }
    }
}