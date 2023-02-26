package com.acxdev.poolguardapps.util

import android.content.Context
import android.content.Intent
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.WorkerList
import com.acxdev.poolguardapps.ui.worker.ActivityWorkerHistory

fun Context.workerOnClick(wallet: Wallet) = object : OnClick<WorkerList> {
    override fun onItemClick(item: WorkerList, position: Int) {
        Intent(this@workerOnClick, ActivityWorkerHistory::class.java)
            .putExtra(ActivityWorkerHistory.WALLET_ID, wallet._id.toString())
            .putExtra(ActivityWorkerHistory.WORKER_NAME, item.name)
            .also {
                startActivity(it)
            }
    }
}

fun Context.paymentOnClick(wallet: Wallet) = object : OnClick<PaymentList>{
    override fun onItemClick(item: PaymentList, position: Int) {
        openPaymentWebView(wallet, item)
    }
}