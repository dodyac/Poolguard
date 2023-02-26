package com.acxdev.poolguardapps.ui.main.setting

import android.view.ViewGroup
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowTextBoxBinding
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.util.getSavedPrefs

class RowTextBoxNew(list: MutableList<Menu>, private val type: String, private val click: OnClick<Menu>) :
    BaseAdapterLib<RowTextBoxBinding, Menu>(RowTextBoxBinding::inflate, list) {

    override fun ViewHolder<RowTextBoxBinding>.bind(item: Menu) {
        scopeLayout {
            name.text = item.title
            name.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

            val condition = context.getSavedPrefs(type).toString()

            if (item.value.toString() == condition) {
                name.apply {
                    setTextColor(context.getColorPrimary())
                    backgroundTint(context.getColor(R.color.blueSecondary))
                }
                card.strokeColor = context.getColorPrimary()
            } else {
                name.apply {
                    setTextColor(context.getColor(R.color.textSecondary))
                    backgroundTint(context.getColor(R.color.textSecondary2))
                }
                card.strokeColor = context.getColor(R.color.textSecondary)
            }

            name.setOnClickListener {
                click.onItemClick(item, adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}