package com.acxdev.poolguardapps.ui.main.news


import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowCategoriesBinding
import com.acxdev.poolguardapps.model.NewsCategoriesResponse

class RowCategories(list: MutableList<NewsCategoriesResponse>, private val click: OnClick<NewsCategoriesResponse>) :
    BaseAdapterLib<RowCategoriesBinding, NewsCategoriesResponse>(RowCategoriesBinding::inflate, list) {

    var selected = 0

    override fun ViewHolder<RowCategoriesBinding>.bind(item: NewsCategoriesResponse) {
        scopeLayout {
            name.text = item.categoryName

            if (selected == adapterPosition) {
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
                if (selected >= 0) {
                    notifyItemChanged(selected)
                }
                selected = adapterPosition
                notifyItemChanged(selected)
                click.onItemClick(item, adapterPosition)
            }
        }
    }
}