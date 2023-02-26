package com.acxdev.poolguardapps.ui.pool

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.ext.toasty
import com.acxdev.commonFunction.util.ext.view.backgroundTint
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.commonFunction.util.ext.view.setText
import com.acxdev.commonFunction.util.ext.view.toEditString
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.SheetAddWalletBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.coin.Coin
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.ui.main.ActivityMain
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.isNotEmpty
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZInsertInto

class SheetAddWallet : BaseBottomSheet<SheetAddWalletBinding>(SheetAddWalletBinding::inflate) {

    private lateinit var symbol: String
    private lateinit var pool: Pool

    override fun SheetAddWalletBinding.configureViews() {
        pool = Pool.values().toList().find { it.value == getStringExtra() }!!

        safeContext {
            sheetToolbar.set(R.string.add_wallet, true) { menu, _ ->
                menu.isClickable = false
                menu.setBackgroundResource(com.acxdev.commonFunction.R.drawable.bg_rounded_extra_small)
                menu.backgroundTint(getColor(R.color.cardBackground2))
                menu.imageUrl(pool.icon)
                menu.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        Constant.SELECTED_COIN = -1

        coin.setHStack(RowCoin(pool.coin.toMutableList(), object : OnClick<Coin> {
            override fun onItemClick(item: Coin, position: Int) {
                symbol = item.crypto.name
            }
        }), hasFixed = true)
    }

    override fun SheetAddWalletBinding.onClickListener() {
        safeContext {
            wallet.setEndIconOnClickListener {
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                clipboard?.let { clipboardManager ->
                    clipboardManager.primaryClipDescription?.let { clipDescription ->
                        if (clipboardManager.hasPrimaryClip() &&
                            (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) || clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                            clipboardManager.primaryClip?.let {
                                val item = it.getItemAt(0)
                                wallet.setText(item.text.toString())
                            }
                        }
                    }
                }
            }
            button.apply {
                onPositive {
                    setOnClickListener {
                        if(Constant.SELECTED_COIN == -1) toasty(Toast.WARNING, R.string.chooseCoinFirst) else {
                            if(name.isNotEmpty() && wallet.isNotEmpty()) {
                                Wallet(pool.value, name.toEditString(), wallet.toEditString(), symbol).also {
                                    sqLiteZInsertInto(Wallet::class.java, it)
                                    toasty(Toast.SUCCESS, R.string.successfullyAddedWallet)
                                }
                                if(requireActivity().isTaskRoot){
                                    Intent(context, ActivityMain::class.java)
                                        .putExtra(Constant.Intent.CURRENT_POSITION,1)
                                        .also {
                                            dismiss()
                                            startActivity(it)
                                            requireActivity().finish()
                                        }
                                } else {
                                    requireActivity().setResult(Constant.DefaultValue.RESULT_WALLET_ADDED)
                                    dismiss()
                                    requireActivity().finish()
                                }
                            }
                        }
                    }
                }

                onNegative {
                    setOnClickListener {
                        dismiss()
                    }
                }
            }
        }
    }
}