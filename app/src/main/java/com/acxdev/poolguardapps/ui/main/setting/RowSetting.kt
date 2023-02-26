package com.acxdev.poolguardapps.ui.main.setting

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.RowSettingBinding
import com.acxdev.poolguardapps.model.SettingTag
import com.acxdev.poolguardapps.model.Settings
import com.acxdev.poolguardapps.util.*
import com.google.android.material.card.MaterialCardView

class RowSetting(
    private val type: Type,
    private val list: MutableList<Settings>,
    private val onBehavior: OnBehavior
) :
    BaseAdapterLib<RowSettingBinding, Settings>(RowSettingBinding::inflate, list) {

    override fun ViewHolder<RowSettingBinding>.bind(item: Settings) {
        scopeLayout {

            title.text = item.title
            subtitle.visibleIf(item.subtitle != null)
            subtitle.text = item.subtitle

            icon.apply {
                setImageResource(item.icon)
                backgroundTint(context.getColor(item.iconBackground))
            }

            arrow.visibleIf(!item.isToggle)
            switcher.visibleIf(item.isToggle)
            if(item.isToggle) {
                switcher.apply {
                    isChecked = when(type){
                        Type.GENERAL -> {
                            when(item.tags) {
                                SettingTag.DEFAULT_BROWSER -> {
                                    context.getSavedPrefs(Constant.SharedPrefs.IS_OPEN_DEFAULT_BROWSER).toString().toBoolean()
                                }
                                else -> false
                            }
                        }
                        Type.APPEARANCE -> {
                            when(item.tags) {
                                SettingTag.THEME -> {
                                    context.getSavedPrefs(ConstantLib.IS_DARK_MODE).toString().toBoolean()
                                }
                                else -> false
                            }
                        }
                        Type.NOTIFICATION -> {
                            when(item.tags) {
                                SettingTag.ALLOW_NOTIFICATION -> {
                                    context.getSavedPrefs(Constant.SharedPrefs.AUTO_CHECK).toString().toBoolean()
                                }
                                SettingTag.WORKER_OFFLINE -> {
                                    context.getSavedPrefs(Constant.SharedPrefs.WORKER_OFFLINE).toString().toBoolean()
                                }
                                SettingTag.PAYMENTS -> {
                                    context.getSavedPrefs(Constant.SharedPrefs.PAYMENT_NOTIFICATION).toString().toBoolean()
                                }
                                else -> false
                            }
                        }
                        else -> false
                    }
                    if(type == Type.NOTIFICATION && adapterPosition != 0) {
                        isEnabled = context.getSavedPrefs(Constant.SharedPrefs.AUTO_CHECK).toString().toBoolean()
                    }
                    setOnCheckedChangeListener { _, isChecked ->
                        onBehavior.onCheckedChange(isChecked, item, adapterPosition)
                        if(type == Type.NOTIFICATION && item.tags == SettingTag.ALLOW_NOTIFICATION) {
                            notifyItemRangeChanged(1, list.size)
                        }
                    }
                }
            } else {
                root.setOnClickListener {
                    onBehavior.onItemClick(item, adapterPosition)
                }
            }
            if(type == Type.NOTIFICATION && adapterPosition != 0) {
                root.enabled(context.getSavedPrefs(Constant.SharedPrefs.AUTO_CHECK).toString().toBoolean())
            }
        }
    }

    interface OnBehavior {
        fun onItemClick(item: Settings, position: Int)
        fun onCheckedChange(state: Boolean, item: Settings, position: Int)
    }

    private fun MaterialCardView.enabled(checked: Boolean){
        val isAmoled = context.getSavedPrefs(Constant.SharedPrefs.COLOR) == context.getString(R.string.midNightAmoled)
        isEnabled = if(checked) {
            setCardBackgroundColor(context.getColor(if(isAmoled) com.acxdev.shimmer.R.color.black else R.color.background))
            true
        } else {
            setCardBackgroundColor(context.getColor(if(isAmoled) R.color.background else R.color.blackSecondary))
            false
        }
    }
}

enum class Type {
    GENERAL,
    NOTIFICATION,
    SUPPORT,
    APPEARANCE,
    JOIN_COMMUNITY
}