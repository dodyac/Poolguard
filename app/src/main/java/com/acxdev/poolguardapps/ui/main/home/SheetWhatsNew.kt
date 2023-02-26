package com.acxdev.poolguardapps.ui.main.home

import android.content.Intent
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.getVersionName
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Changelog
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.SheetWhatsNewBinding
import com.acxdev.poolguardapps.model.Field
import com.acxdev.poolguardapps.model.WhatsNew
import com.acxdev.poolguardapps.ui.list.ActivityList

class SheetWhatsNew: BaseBottomSheet<SheetWhatsNewBinding>(SheetWhatsNewBinding::inflate) {

    override fun SheetWhatsNewBinding.configureViews() {
        safeContext {
            sheetToolbar.set("What's new in ${getString(R.string.app_name)}") { _, subtitle ->
                subtitle.visible()
                subtitle.text = "${getString(R.string.version)} ${getVersionName()}"
            }

            val whatsNewList = mutableListOf<WhatsNew>()

            //TODO Raw String
            val new = mutableListOf<Field>()
            new.add(Field("Remove Ads","Removed all ads, now Poolguard is completely FREE!"))
            new.add(Field("EthereumPoW (ETHW)","Added EthereumPoW (ETHW) after Ethereum migration."))

            val removed = mutableListOf<Field>()
            removed.add(Field("Inactive Pools","Removed some inactive pools."))

            val improve = mutableListOf<Field>()
            improve.add(Field("Performance","Enjoy better performance with improved functionality."))

            val fix = mutableListOf<Field>()
            fix.add(Field("Android 13","Fixed the function not working properly on Android 13."))

            whatsNewList.add(WhatsNew(Changelog.NEW, new))
            whatsNewList.add(WhatsNew(Changelog.IMPROVED, improve))
            whatsNewList.add(WhatsNew(Changelog.FIX, fix))
            whatsNewList.add(WhatsNew(Changelog.REMOVED, removed))
            whatsNew.setVStack(RowWhatsNew(whatsNewList), true)
        }
    }

    override fun SheetWhatsNewBinding.onClickListener() {
        safeContext {
            button.apply {
                onPositive {
                    text = getString(R.string.got_it)
                    setOnClickListener {
                        dismiss()
                    }
                }

                onNegative {
                    text = getString(R.string.learn_more)
                    backgroundTint(getColor(R.color.cardBackground))
                    setTextColor(getColorPrimary())
                    rippleColor = getColorStateList(R.color.primaryBlue)
                    setOnClickListener {
                        Intent(context, ActivityList::class.java)
                            .putExtra(ConstantLib.DATA, ActivityList.CHANGELOG)
                            .also {
                                startActivity(it)
                                dismiss()
                            }
                    }
                }
            }
        }
    }
}