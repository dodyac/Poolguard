package com.acxdev.poolguardapps.ui.list

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowCurrencyBinding
import com.acxdev.poolguardapps.model.DefaultScreen
import com.acxdev.poolguardapps.util.getSavedPrefs

class RowDefaultScreen(list: MutableList<DefaultScreen>, private val onClick: OnClick<DefaultScreen>)
    : BaseAdapterLib<RowCurrencyBinding, DefaultScreen>(RowCurrencyBinding::inflate, list) {

    override fun ViewHolder<RowCurrencyBinding>.bind(item: DefaultScreen) {
        scopeLayout {
            country.text = item.title
            currency.text = item.sub

            card.isChecked = if(context.getSavedPrefs(Constant.SharedPrefs.IS_WALLET_SCREEN).toString().toBoolean()) {
                if(adapterPosition > 4) {
                    item.value == context.getSavedPrefs(Constant.SharedPrefs.SCREEN_DEFAULT).toString().toInt()
                } else {
                    false
                }
            } else {
                if(adapterPosition < 5) {
                    item.value == context.getSavedPrefs(Constant.SharedPrefs.SCREEN_DEFAULT).toString().toInt()
                } else {
                    false
                }
            }

            card.setOnClickListener {
                notifyItemChanged(adapterPosition)
                onClick.onItemClick(item, adapterPosition)
            }
        }
    }
}