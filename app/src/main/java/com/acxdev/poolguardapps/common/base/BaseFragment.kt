package com.acxdev.poolguardapps.common.base

import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.common.base.BaseFragmentLib
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.ui.dialog.DialogProgress

abstract class BaseFragment<VB : ViewBinding>(inflateViewGroup: InflateViewGroup<VB>) : BaseFragmentLib<VB>(inflateViewGroup) {

    protected lateinit var dialogProgress: DialogProgress
    protected lateinit var currency: String

    override fun onCreate(savedInstanceState: Bundle?) {
        safeContext {
            useCurrentTheme()
        }
        dialogProgress = DialogProgress()
        currency = prefs(Constant.SharedPrefs.CURRENCY)
        super.onCreate(savedInstanceState)
    }

    protected fun showDialog() {
        dialogProgress.show(childFragmentManager, Constant.TAG_DIALOG)
    }

    protected fun dismissDialog() {
        dialogProgress.dismiss()
    }

    fun prefs(path: String): String = requireContext().getSavedPrefs(path).toString()
}