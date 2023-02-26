package com.acxdev.poolguardapps.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.acxdev.commonFunction.util.ext.getColorPrimary
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.acxdev.commonFunction.util.ext.webView
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseUrl.explorer
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Wallet

fun Context.openWebView(url: String) {
    if (getSavedPrefs(Constant.SharedPrefs.IS_OPEN_DEFAULT_BROWSER).toString().toBoolean()) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    } else {
        getCompatActivity().webView(url, getColorPrimary())
    }
}

fun Context.openPaymentWebView(wallet: Wallet, list: PaymentList) {
    openWebView(wallet.explorer(list))
}