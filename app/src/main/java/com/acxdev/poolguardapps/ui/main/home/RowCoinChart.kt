package com.acxdev.poolguardapps.ui.main.home

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.toDateEpoch
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.util.applyStyleDisabled
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.databinding.RowCoinChartBinding
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.initRedGreenIndicator
import com.acxdev.poolguardapps.util.lineDataSetFilledColor
import com.acxdev.poolguardapps.model.Raw
import com.acxdev.poolguardapps.model.coin.CoinChart
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.toLocalCurrency
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*

class RowCoinChart(list: MutableList<CoinChart>, private val price: Map<String, Map<String, Raw>>) :
    BaseAdapterLib<RowCoinChartBinding, CoinChart>(RowCoinChartBinding::inflate, list) {

    override fun ViewHolder<RowCoinChartBinding>.bind(item: CoinChart) {
        val priceByPosition =
            price[item.symbol]!![context.getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()]!!

        scopeLayout {
            icon.imageUrl(item.symbol.crypto().icon)
            name.text = item.symbol.crypto().fullName
            price.text = priceByPosition.PRICE.toLocalCurrency(context)

            val charts = item.chart.map { it[0].toLong().toDateEpoch(Constant.HOUR_MINUTE, Locale.ENGLISH) }
            val data = item.chart.map { it[1] }
            val filtered = data.filterIndexed { index, _ -> index % 10 == 0 }
            val isMinus = priceByPosition.CHANGEPCT24HOUR < 1

            initRedGreenIndicator(
                percent,
                arrow,
                priceByPosition.CHANGEPCT24HOUR,
                isMinus
            )

            val dataset = mutableListOf<ILineDataSet>()
            dataset.add(
                context.lineDataSetFilledColor(
                    emptyString(),
                    filtered,
                    if (isMinus) R.color.red else R.color.green,
                    if (isMinus) R.color.redSecondary else R.color.green
                )
            )

            chart.applyStyleDisabled(charts, dataset)
        }
    }
}