package com.acxdev.poolguardapps.ui.payout

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowPayoutBinding
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.util.*

class RowPayout(
    private val list: MutableList<PaymentList>,
    private val isViewAll: Boolean,
    private val wallet: Wallet,
    private val onClick: OnClick<PaymentList>
) :
    BaseAdapterLib<RowPayoutBinding, PaymentList>(RowPayoutBinding::inflate, list) {

    override fun ViewHolder<RowPayoutBinding>.bind(item: PaymentList) {
        scopeLayout {
            coinImage.imageUrl(item.symbol.crypto().icon)

            amount.text =
                item.amount.toCrypto(wallet.symbol, CryptoFormat.DIVIDE_BLOCK_SYMBOL)

            poolTx.text =
                context.getString(R.string.stripes, wallet.pool.removeSuffixPool(), item.txHash ?: item.kernelId)

            date.text = if(wallet.pool == Pool.LUCKPOOL.value) {
                item.paidOn.toTimesAgo(context, false)
            } else {
                item.paidOn.toTimesAgo(context)
            }
        }
        itemView.setOnClickListener {
            onClick.onItemClick(item, adapterPosition)
        }
    }

    override fun getItemCount(): Int =
        if (!isViewAll && list.size > Constant.DefaultValue.MAX_PAYMENTS) {
            Constant.DefaultValue.MAX_PAYMENTS
        } else {
            list.size
        }
}