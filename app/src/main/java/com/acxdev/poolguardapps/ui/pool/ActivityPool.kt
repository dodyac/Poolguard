package com.acxdev.poolguardapps.ui.pool

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.util.ext.showSheetWithExtra
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityPoolBinding
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.repository.PoolStatus

class ActivityPool : BaseActivity<ActivityPoolBinding>(ActivityPoolBinding::inflate) {

    override fun ActivityPoolBinding.configureViews() {
        toolbar.set(R.string.choosePool)

        val adapter = RowPool(Pool.values().toList()
            .sortedBy { it.value.lowercase() }
            .filter { it.isListed && it.poolStatus == PoolStatus.ACTIVE },
            object : OnClick<Pool> {
                override fun onItemClick(item: Pool, position: Int) {
                    search.clearFocus()
                    showSheetWithExtra(SheetAddWallet(), item.value)
                }
            }, object : OnFilter<Pool> {
                override fun onFilteredResult(list: List<Pool>) {
                    pool.visibleIf(list.isNotEmpty())
                    empty.visibleIf(list.isEmpty())
                }
            })

        pool.setVStack(adapter, hasFixed = true)

        search.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)

                emptyTitle.text = getString(R.string.couldFind, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun ActivityPoolBinding.onClickListener() {
        bottom.setOnClickListener {
            // TODO onclik request
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}