package com.acxdev.poolguardapps.ui.main.profitability

import android.view.ViewGroup
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowTextBoxBinding
import com.acxdev.poolguardapps.util.getSavedPrefs

class RowTextBox(
    private val list: MutableList<String>,
    private val type: String,
    private val isAlgorithm: Boolean,
    private val isViewAll: Boolean,
    private val click: OnClick<String>
) :
    BaseAdapterLib<RowTextBoxBinding, String>(RowTextBoxBinding::inflate, list) {

    override fun ViewHolder<RowTextBoxBinding>.bind(item: String) {
        scopeLayout {
            name.text = item
            name.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

            val condition = if(isAlgorithm) {
                context.getSavedPrefs(type).toString()
            } else {
                context.getSavedPrefs(type).toString().fromSpan()
            }

            if (item == condition) {
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

    private fun String.fromSpan() = when(this){
        "1h" -> context.getString(R.string.oneHour)
        "24h" -> context.getString(R.string.oneDay)
        "3" -> context.getString(R.string.days, 3)
        "7" -> context.getString(R.string.days, 7)
        else -> context.getString(R.string.current)
    }
    override fun getItemCount(): Int =
        if (!isViewAll && list.size > Constant.DefaultValue.MAX_FLEX) {
            Constant.DefaultValue.MAX_FLEX
        } else {
            list.size
        }
}