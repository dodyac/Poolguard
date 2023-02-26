package com.acxdev.poolguardapps.ui.dialog

import androidx.lifecycle.lifecycleScope
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseDialog
import com.acxdev.poolguardapps.databinding.DialogProgressBinding
import kotlinx.coroutines.delay

class DialogProgress : BaseDialog<DialogProgressBinding>(DialogProgressBinding::inflate) {

    override fun DialogProgressBinding.configureViews() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            try {
                delay(Constant.DefaultValue.DELAY_MAX_LOADING)
                dismiss()
            } catch (e: Exception) {
                dismiss()
                e.printStackTrace()
            }
        }
    }

    override fun DialogProgressBinding.onClickListener() {

    }
}
