package com.acxdev.poolguardapps.ui.main.setting

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowColorBinding
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.util.getSavedPrefs

class RowColor(list: MutableList<Menu>, private val onClick: OnClick<Menu>)
    : BaseAdapterLib<RowColorBinding, Menu>(RowColorBinding::inflate, list) {

    override fun ViewHolder<RowColorBinding>.bind(item: Menu) {
        scopeLayout {
            val color = context.getColor(item.value.toString().toInt())
            icon.backgroundTint(color)
            subtitleColor.backgroundTint(color)
            titleColor.backgroundTint(color)

            val currentStyle = context.getSavedPrefs(Constant.SharedPrefs.COLOR)

            card.apply {
                if (currentStyle == item.title) {
                    strokeWidth = context.resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x3dp)
                    strokeColor = color
                } else {
                    strokeWidth = context.resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x0dp)
                }
                invalidate()
            }

            value.text = item.title
            value.setTextColor(color)

            itemView.setOnClickListener {
                onClick.onItemClick(item, adapterPosition)
            }
        }
    }
}