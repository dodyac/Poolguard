package com.acxdev.poolguardapps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.model.Notification
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.model.gpu.GPUDatabase
import com.acxdev.poolguardapps.ui.dashboard.ActivityDashboard
import com.acxdev.poolguardapps.ui.main.ActivityMain
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZCreateTables

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstTime = getSavedPrefs(Constant.SharedPrefs.IS_FIRST_TIME).toString().isEmpty()
        sqLiteZCreateTables(
            Wallet::class.java,
            Notification::class.java,
            GPUDatabase::class.java
        )
        val isWalletScreen =
            getSavedPrefs(Constant.SharedPrefs.IS_WALLET_SCREEN).toString().toBoolean()
        val defaultScreen = getSavedPrefs(Constant.SharedPrefs.SCREEN_DEFAULT).toString()

        if (isWalletScreen) {
            Intent(this, ActivityDashboard::class.java)
                .putExtra(Constant.Intent.ID, defaultScreen)
                .also {
                    startActivity(it)
                    finish()
                }
        } else {
            Intent(this, if (isFirstTime) ActivityMain::class.java else ActivityMain::class.java)
                .also {
                    startActivity(it)
                    finish()
                }
        }
    }
}