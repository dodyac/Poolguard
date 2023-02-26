package com.acxdev.poolguardapps.ui.dashboard

import androidx.lifecycle.lifecycleScope
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.SheetDeleteWalletBinding
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.util.crypto
import com.acxdev.poolguardapps.util.imageUrl
import com.acxdev.poolguardapps.util.pool
import com.acxdev.sqlitez.SqliteZ.Companion.sqliteZDeleteById
import kotlinx.coroutines.delay

class SheetDeleteWallet(private val onSheetDeleteWallet: OnSheetDeleteWallet)
    : BaseBottomSheet<SheetDeleteWalletBinding>(SheetDeleteWalletBinding::inflate) {

    private var second = Constant.DefaultValue.DURATION_DELETE_WALLET
    private lateinit var wallet: Wallet

    override fun SheetDeleteWalletBinding.configureViews() {
        sheetToolbar.set(R.string.areYouSureWannaDeleteWallet)

        wallet = getExtraAs(Wallet::class.java)

        pool.imageUrl(wallet.pool().icon)
        coinIcon.imageUrl(wallet.symbol.crypto().icon)
        name.text = wallet.name
        address.text = wallet.address

        lifecycleScope.launchWhenCreated {
            button.negative.isEnabled = false
            repeat(Constant.DefaultValue.DURATION_DELETE_WALLET - 1) {
                second -= 1
                button.negative.text = getString(R.string.delete) + " ($second)"
                delay(Constant.DefaultValue.DELAY_UPDATE_VALUE)
            }
            button.negative.isEnabled = true
            button.negative.text = getString(R.string.delete)
        }
    }

    override fun SheetDeleteWalletBinding.onClickListener() {
        safeContext {
            button.apply {
                onNegative {
                    setOnClickListener {
                        sqliteZDeleteById(Wallet::class.java, wallet._id!!.toLong())
                        dismiss()
                        onSheetDeleteWallet.onComplete(wallet._id!!)
                    }
                }

                onPositive {
                    text = getString(R.string.cancel)
                    setOnClickListener {
                        dismiss()
                    }
                }
            }
        }
    }

    interface OnSheetDeleteWallet {
        fun onComplete(id: Long)
    }
}