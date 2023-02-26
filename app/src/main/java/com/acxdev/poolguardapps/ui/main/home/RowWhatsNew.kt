package com.acxdev.poolguardapps.ui.main.home

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Changelog
import com.acxdev.poolguardapps.databinding.RowWhatsNewBinding
import com.acxdev.poolguardapps.model.WhatsNew

class RowWhatsNew(list: MutableList<WhatsNew>) :
    BaseAdapterLib<RowWhatsNewBinding, WhatsNew>(RowWhatsNewBinding::inflate, list) {

    override fun ViewHolder<RowWhatsNewBinding>.bind(item: WhatsNew) {
        scopeLayout {
            tag.apply {
                text = item.tag.value

                when (item.tag) {
                    Changelog.NEW -> {
                        backgroundTint(context.getColor(R.color.blueSecondary))
                        setTextColor(context.getColor(R.color.primaryBlue))
                    }
                    Changelog.IMPROVED -> {
                        backgroundTint(context.getColor(R.color.greenSecondary))
                        setTextColor(context.getColor(R.color.green))
                    }
                    Changelog.FIX -> {
                        backgroundTint(context.getColor(R.color.yellowSecondary))
                        setTextColor(context.getColor(R.color.yellow))
                    }
                    Changelog.REMOVED -> {
                        backgroundTint(context.getColor(R.color.redSecondary))
                        setTextColor(context.getColor(R.color.red))
                    }
                }
            }
            field.setVStack(RowField(item.field), true)
        }
    }
}