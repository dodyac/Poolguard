package com.acxdev.poolguardapps.ui.main.home

import android.widget.LinearLayout
import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.util.ext.view.setHtml
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowFieldBinding
import com.acxdev.poolguardapps.model.Field
import java.util.*

class RowField(val list: MutableList<Field>) :
    BaseAdapterLib<RowFieldBinding, Field>(RowFieldBinding::inflate, list) {

    override fun ViewHolder<RowFieldBinding>.bind(item: Field) {
        scopeLayout {
            title.text = item.title
            subtitle.setHtml(item.subtitle)

            if (adapterPosition == list.size - 1) {
                val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                param.bottomMargin = context.resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.padding_parent)
                subtitle.layoutParams = param
            }
        }
    }
}