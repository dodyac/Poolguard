package com.acxdev.poolguardapps.ui.list

import com.acxdev.commonFunction.adapter.BaseAdapterLib
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.databinding.RowTranslatorBinding
import com.acxdev.poolguardapps.model.Translator

class RowTranslator(list: MutableList<Translator>) :
    BaseAdapterLib<RowTranslatorBinding, Translator>(RowTranslatorBinding::inflate, list) {

    override fun ViewHolder<RowTranslatorBinding>.bind(item: Translator) {
        scopeLayout {
            language.text = item.language
            name.text = context.getString(R.string.translatedBy, item.name)
            releaseVersion.text = context.getString(
                R.string.spacer,
                context.getString(R.string.version),
                item.releaseVersion
            )
        }
    }
}