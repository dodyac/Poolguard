package com.acxdev.poolguardapps.ui.main.setting

import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.toPercent
import com.acxdev.commonFunction.util.ext.toasty
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.SheetHashRateBinding

class SheetHashRate(private val onSheetHashRate: OnSheetHashRate)
    : BaseBottomSheet<SheetHashRateBinding>(SheetHashRateBinding::inflate) {

    override fun SheetHashRateBinding.configureViews() {
        sheetToolbar.set(R.string.hashrateDrop)

        percent.value = prefs(Constant.SharedPrefs.HASHRATE_DROP).toFloat()

        //TODO Raw String
        percent.setLabelFormatter { "Hashrate drop by: ${String.format("%.0f", it)}%" }
    }

    override fun SheetHashRateBinding.onClickListener() {
        button.apply {
            onPositive {
                setOnClickListener {
                    safeContext {
                        putPrefs(Constant.SharedPrefs.HASHRATE_DROP, percent.value)
                    }
                    onSheetHashRate.onComplete(percent.value.toPercent())
                    toasty(Toast.SUCCESS, R.string.settingSaved)
                    dismiss()
                }
            }

            onNegative {
                setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    interface OnSheetHashRate{
        fun onComplete(data: String)
    }
}