package com.acxdev.poolguardapps.ui.chart

import android.content.res.ColorStateList
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowCategoriesBinding
import com.acxdev.poolguardapps.model.Menu

class RowTime(
    list: MutableList<Menu>,
    private val click: OnClick<Menu>? = null
) :
    BaseAdapterLib<RowCategoriesBinding, Menu>(RowCategoriesBinding::inflate, list) {

    private var selected = 0
    override fun ViewHolder<RowCategoriesBinding>.bind(item: Menu) {
        scopeLayout {
            name.text = item.title

            if (selected == adapterPosition) {
                name.apply {
                    setTextColor(context.getColor(com.acxdev.commonFunction.R.color.white))
                    backgroundTintList = ColorStateList.valueOf(context.getColorPrimary())
                }
            } else {
                name.apply {
                    setTextColor(context.getColor(R.color.text))
                    backgroundTint(context.getColor(R.color.background))
                }
            }

            name.setOnClickListener {
                if (selected >= 0) {
                    notifyItemChanged(selected)
                }
                selected = adapterPosition
                notifyItemChanged(selected)
                click?.onItemClick(item, adapterPosition)
            }
        }
    }
}