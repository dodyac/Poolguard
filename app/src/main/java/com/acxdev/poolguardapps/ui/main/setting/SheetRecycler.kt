package com.acxdev.poolguardapps.ui.main.setting

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.LayoutEmptyLottieBinding
import com.acxdev.poolguardapps.databinding.SheetRecyclerBinding

class SheetRecycler(@StringRes private val title: Int, private val layout: ((RecyclerView, LayoutEmptyLottieBinding) -> Unit))
    : BaseBottomSheet<SheetRecyclerBinding>(SheetRecyclerBinding::inflate) {

    override fun SheetRecyclerBinding.configureViews() {
        sheetToolbar.set(title)

        layout.invoke(recycler, empty)
    }

    override fun SheetRecyclerBinding.onClickListener() {}
}