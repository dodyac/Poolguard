package com.acxdev.poolguardapps.ui.dashboard

import com.acxdev.commonFunction.adapter.BaseSliderLib
import com.acxdev.commonFunction.util.ext.toZero
import com.acxdev.poolguardapps.databinding.RowSlideBinding
import com.acxdev.poolguardapps.model.Menu

class RowSlide(list: MutableList<Menu>) :
    BaseSliderLib<RowSlideBinding, Menu>(RowSlideBinding::inflate, list) {

    override fun ViewHolder<RowSlideBinding>.bind(item: Menu) {
        scopeLayout {
            title.text = item.title
            value.text = item.value.toZero()
        }
    }
}