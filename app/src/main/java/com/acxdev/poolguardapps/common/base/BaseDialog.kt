package com.acxdev.poolguardapps.common.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.common.base.BaseDialogLib
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.acxdev.poolguardapps.util.getSavedPrefs

abstract class BaseDialog<VB : ViewBinding>(inflateViewGroup: InflateViewGroup<VB>) : BaseDialogLib<VB>(inflateViewGroup) {

    override fun onCreate(savedInstanceState: Bundle?) {
        safeContext {
            useCurrentTheme()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun prefs(path: String): String = requireContext().getSavedPrefs(path).toString()
}