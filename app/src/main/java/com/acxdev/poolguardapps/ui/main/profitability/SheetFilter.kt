package com.acxdev.poolguardapps.ui.main.profitability

import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.view.setFlex
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.LayoutRecyclerViewAllBinding
import com.acxdev.poolguardapps.databinding.SheetFilterProfitabilityBinding
import com.acxdev.poolguardapps.repository.MyAlgorithm

class SheetFilter(private val onSheetFilter: OnSheetFilter) :
    BaseBottomSheet<SheetFilterProfitabilityBinding>(SheetFilterProfitabilityBinding::inflate) {

    override fun SheetFilterProfitabilityBinding.configureViews() {
        safeContext {
            sheetToolbar.set(R.string.filter, true){ menu, _ ->
                menu.setImageResource(R.drawable.icons8_restore)
                menu.setOnClickListener {
                    putPrefs(Constant.SharedPrefs.ALGORITHM_STATE, MyAlgorithm.Ethash.name)
                    putPrefs(Constant.SharedPrefs.DIFFICULTY_STATE, "24h")
                    putPrefs(Constant.SharedPrefs.BLOCK_REWARD_STATE, "24h")
                    initRecycler()
                }
            }

            scroll.setMaxHeightDensity(com.acxdev.commonFunction.R.dimen.x400dp)

            initRecycler()
        }
    }

    override fun SheetFilterProfitabilityBinding.onClickListener() {
        button.onPositive {
            text = getString(R.string.calculate)
            setOnClickListener {
                dismiss()
                onSheetFilter.onComplete()
            }
        }
    }

    private fun initRecycler() {
        scopeLayout {
            block.initRecycler(
                getString(R.string.blockReward),
                Constant.SharedPrefs.BLOCK_REWARD_STATE,
                listOf(
                    getString(R.string.current),
                    getString(R.string.oneHour),
                    getString(R.string.oneDay),
                    getString(R.string.days, 3),
                    getString(R.string.days,7)
                ),
                blockRewardOnClick
            )

            difficulty.initRecycler(
                getString(R.string.difficulty),
                Constant.SharedPrefs.DIFFICULTY_STATE,
                listOf(
                    getString(R.string.current),
                    getString(R.string.oneHour),
                    getString(R.string.oneDay),
                    getString(R.string.days, 3),
                    getString(R.string.days,7)
                ),
                diffOnClick
            )

            algorithm.initRecycler(
                getString(R.string.algorithm),
                Constant.SharedPrefs.ALGORITHM_STATE,
                MyAlgorithm.values().filter { it.isGpu }.map { it.name },
                algorithmOnClick,
                true
            )
        }
    }

    private val blockRewardOnClick = object : OnClick<String> {
        override fun onItemClick(item: String, position: Int) {
            safeContext {
                putPrefs(Constant.SharedPrefs.BLOCK_REWARD_STATE, position.toSpan())
            }
        }
    }

    private val diffOnClick = object : OnClick<String> {
        override fun onItemClick(item: String, position: Int) {
            safeContext {
                putPrefs(Constant.SharedPrefs.DIFFICULTY_STATE, position.toSpan())
            }
        }
    }

    private val algorithmOnClick = object : OnClick<String> {
        override fun onItemClick(item: String, position: Int) {
            safeContext {
                putPrefs(Constant.SharedPrefs.ALGORITHM_STATE, item)
            }
        }
    }

    private fun LayoutRecyclerViewAllBinding.initRecycler(
        titles: String,
        type: String,
        list: List<String>,
        onClick: OnClick<String>,
        isAlgorithm: Boolean = false
    ) {
        text.text = titles
        recycler.setFlex(RowTextBox(list.toMutableList(), type, isAlgorithm,false, onClick))
        viewAll.visibleIf(list.size > 5)
        var isViewAll = false
        viewAll.setOnClickListener {
            recycler.setFlex(RowTextBox(list.toMutableList(), type, isAlgorithm, !isViewAll, onClick))
            isViewAll = !isViewAll
            viewAll.text = if(isViewAll) getString(R.string.hide) else getString(R.string.viewAll)
        }
    }

    private fun Int.toSpan() = when(this) {
        1 -> "1h"
        2 -> "24h"
        3 -> "3"
        4 -> "7"
        else -> ""
    }

    interface OnSheetFilter {
        fun onComplete()
    }
}