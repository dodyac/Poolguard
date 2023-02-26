package com.acxdev.poolguardapps.common.base

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.common.base.BaseBottomSheetLib
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.LayoutButtonChoiceBinding
import com.acxdev.poolguardapps.databinding.LayoutToolbarSheetBinding
import com.acxdev.poolguardapps.ui.dialog.DialogProgress
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.google.android.material.button.MaterialButton

abstract class BaseBottomSheet<VB : ViewBinding>
    (inflateViewGroup: InflateViewGroup<VB>, canCancel: Boolean = true, isFullScreen: Boolean = false)
    : BaseBottomSheetLib<VB>(R.style.CustomBottomSheetDialogTheme, inflateViewGroup, canCancel, isFullScreen) {

    private lateinit var dialogProgress: DialogProgress

    override fun onCreate(savedInstanceState: Bundle?) {
        safeContext {
            useCurrentTheme()
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        super.onCreate(savedInstanceState)

        dialogProgress = DialogProgress()
    }

    protected fun showDialog() {
        dialogProgress.show(childFragmentManager, Constant.TAG_DIALOG)
    }

    protected fun dismissDialog() {
        dialogProgress.dismiss()
    }

    protected fun LayoutToolbarSheetBinding.set(
        @StringRes titleSheet: Int,
        isMenuVisible: Boolean = false,
        menuAndSubtitle: ((ImageButton, TextView) -> Unit)? = null,
    ) {
        title.text = getString(titleSheet)

        menu.visibleIf(isMenuVisible, true)

        menuAndSubtitle?.invoke(menu, subtitle)

        close.setOnClickListener {
            dismiss()
        }
    }

    protected fun LayoutToolbarSheetBinding.set(
        titleSheet: String,
        isMenuVisible: Boolean = false,
        menuAndSubtitle: ((ImageButton, TextView) -> Unit)? = null,
    ) {
        title.text = titleSheet

        menu.visibleIf(isMenuVisible, true)

        menuAndSubtitle?.invoke(menu, subtitle)

        close.setOnClickListener {
            dismiss()
        }
    }

    protected fun LayoutButtonChoiceBinding.onNegative(button: MaterialButton.() -> Unit) {
        negative.visible()
        button.invoke(negative)
    }

    protected fun LayoutButtonChoiceBinding.onPositive(button: MaterialButton.() -> Unit) {
        positive.visible()
        button.invoke(positive)
    }

    fun prefs(path: String): String = requireContext().getSavedPrefs(path).toString()
}