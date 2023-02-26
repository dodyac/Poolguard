package com.acxdev.poolguardapps.ui.main.setting

import com.acxdev.commonFunction.adapter.BaseAdapterFilterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowCurrencyBinding
import com.acxdev.poolguardapps.util.getSavedPrefs
import java.util.*

class RowCurrency(list: List<Locale>, private val onClick: OnClick<Locale>, onFilter: OnFilter<Locale>)
    : BaseAdapterFilterLib<RowCurrencyBinding, Locale> (RowCurrencyBinding::inflate, list, onFilter) {

    override fun ViewHolder<RowCurrencyBinding>.bind(item: Locale) {
        val currencies = Currency.getInstance(item)
        scopeLayout {
            currency.text = currencies.currencyCode
            country.text = currencies.getDisplayName(Locale(context.getSavedPrefs(Constant.SharedPrefs.CURRENT_LOCALE).toString()))
            code.text = currencies.getSymbol(item)

            card.isChecked = currencies.currencyCode == context.getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()

            card.setOnClickListener {
                notifyItemChanged(adapterPosition)
                onClick.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun filterBy(item: Locale, query: String): Boolean {
        val currency = Currency.getInstance(item)
        val displayName = currency.getDisplayName(Locale(context.getSavedPrefs(Constant.SharedPrefs.CURRENT_LOCALE).toString())).lowercase()
        val countryCode = currency.currencyCode.lowercase()

        return displayName.contains(query) || countryCode.contains(query)
    }
}