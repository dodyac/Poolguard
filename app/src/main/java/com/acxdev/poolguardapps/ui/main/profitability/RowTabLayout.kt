package com.acxdev.poolguardapps.ui.main.profitability

import android.view.ViewGroup
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowCategoriesBinding
import com.acxdev.poolguardapps.util.getSavedPrefs

class RowTabLayout(list: MutableList<String>, private val click: OnClick<String>) :
    BaseAdapterLib<RowCategoriesBinding, String>(RowCategoriesBinding::inflate, list) {

    override fun ViewHolder<RowCategoriesBinding>.bind(item: String) {
        scopeLayout {
            name.text = item
            name.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            if (context.getSavedPrefs(Constant.SharedPrefs.CALCULATOR_STATE).toString().toInt() == adapterPosition) {
                name.apply {
                    setTextColor(context.getColor(R.color.text))
                    backgroundTint(context.getColor(R.color.cardBackground))
                }
            } else {
                name.apply {
                    setTextColor(context.getColor(R.color.textSecondary))
                    backgroundTint(context.getColor(R.color.backgroundSecondary))
                }
            }

            name.setOnClickListener {
                click.onItemClick(item, adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}