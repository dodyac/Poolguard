package com.acxdev.poolguardapps.ui.list

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowTimeLineBinding
import com.acxdev.poolguardapps.model.TimeLine
import com.acxdev.poolguardapps.ui.main.home.RowField
import com.acxdev.poolguardapps.util.toTimesAgo

class RowTimeLine(private val list: MutableList<TimeLine>) :
    BaseAdapterLib<RowTimeLineBinding, TimeLine>(RowTimeLineBinding::inflate, list) {

    override fun ViewHolder<RowTimeLineBinding>.bind(item: TimeLine) {
        scopeLayout {
            icon.bringToFront()
            version.text = context.getString(
                R.string.spacer,
                context.getString(R.string.version),
                item.version
            )
            date.text = item.date.toTimesAgo(context)
            icon.setImageResource(item.icon)
            field.setVStack(RowField(item.field.toMutableList()), hasFixed = true)
            line.visibleIf(adapterPosition != list.size - 1)
        }
    }
}