package com.acxdev.poolguardapps.ui.wallet

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowMenuBinding

class RowMenu(private val list: MutableList<String>, private val click: OnClick<String>, private val redMenu: Boolean = false)
    : BaseAdapterLib<RowMenuBinding, String>(RowMenuBinding::inflate, list) {

    override fun ViewHolder<RowMenuBinding>.bind(item: String) {
        scopeLayout {
            text.text = item

            if (redMenu && adapterPosition == Constant.DefaultValue.OPTION_MENU) {
                text.setTextColor(context.getColor(R.color.red))
            }

            line.visibleIf(adapterPosition != list.size - 1)

            card.setOnClickListener {
                click.onItemClick(item, adapterPosition)
            }
        }
    }
}