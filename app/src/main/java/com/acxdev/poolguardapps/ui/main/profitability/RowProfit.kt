package com.acxdev.poolguardapps.ui.main.profitability

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.poolguardapps.databinding.RowProfitBinding
import com.acxdev.poolguardapps.model.Profit
import com.acxdev.poolguardapps.util.calculateLocalCurrency
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.zeroLocalCurrency

class RowProfit(
    private val list: MutableList<Profit>,
    private val price: Double
) :
    BaseAdapterLib<RowProfitBinding, Profit>(RowProfitBinding::inflate, list) {

    override fun ViewHolder<RowProfitBinding>.bind(item: Profit) {
        scopeLayout {
            coinIcon.imageUrl(item.symbol.crypto().icon)
            algorithm.text = item.algorithm
            nameSymbol.text = "${item.symbol.crypto().fullName} (${item.symbol})"
            revenue.text = item.revenue.toLocalCurrency()
            profit.text = item.profit.toLocalCurrency()
        }
    }

    private fun String.toLocalCurrency(): String {
        return try {
            //TODO Raw String
            replace("$","").toDouble().calculateLocalCurrency(price, context)
        } catch (e: Exception) {
            e.printStackTrace()
            context.zeroLocalCurrency()
        }
    }
}