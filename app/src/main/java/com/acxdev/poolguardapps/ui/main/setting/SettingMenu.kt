package com.acxdev.poolguardapps.ui.main.setting

import android.content.Context
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.util.ext.getVersionName
import com.acxdev.commonFunction.util.ext.toPercent
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.model.DefaultScreen
import com.acxdev.poolguardapps.model.SettingTag
import com.acxdev.poolguardapps.model.Settings
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.List.listOfLanguage
import com.acxdev.poolguardapps.repository.List.listOfRefreshPeriod
import com.acxdev.poolguardapps.repository.List.listOfScreen
import com.acxdev.poolguardapps.repository.List.listOfStyle
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.listOfCurrency
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import java.util.*

object SettingMenu {

    fun Fragment.safeContext(result: Context.() -> Unit) {
        context?.let {
            result.invoke(it)
        }
    }

    private fun Fragment.prefs(path: String): String = requireContext().getSavedPrefs(path).toString()

    fun Fragment.general(): MutableList<Settings> {
        val setting = mutableListOf<Settings>()

        safeContext {
            val currency = prefs(Constant.SharedPrefs.CURRENCY)
            val currentCurrency = Currency.getInstance(listOfCurrency().find { Currency.getInstance(it).currencyCode == currency })

            setting.add(Settings(
                SettingTag.CURRENCY,
                getString(R.string.currency),
                R.drawable.icons8_money_2,
                R.color.blue2,
                false,
                "${currentCurrency.currencyCode}    ${currentCurrency.getDisplayName(Locale(prefs(Constant.SharedPrefs.CURRENT_LOCALE)))}"
            ))

            val currentScreen = if(prefs(Constant.SharedPrefs.IS_WALLET_SCREEN).toBoolean()) {
                val menu = mutableListOf<DefaultScreen>()
                val walletList = sqLiteZSelectTable(Wallet::class.java)
                walletList.forEach {
                    menu.add(DefaultScreen(it.name, getString(R.string.wallet), it._id!!.toInt()))
                }
                val finder = menu.find { it.value == prefs(Constant.SharedPrefs.SCREEN_DEFAULT).toInt() }?.title
                finder ?: listOfScreen()[0].title
            } else {
                val finder = listOfScreen().find { it.value == prefs(Constant.SharedPrefs.SCREEN_DEFAULT).toInt() }?.title
                finder ?: listOfScreen()[0].title
            }

            setting.add(Settings(
                SettingTag.DEFAULT_SCREEN,
                getString(R.string.defaultScreen),
                R.drawable.icons8_external_link,
                com.acxdev.shimmer.R.color.default_color,
                false,
                currentScreen
            ))

            setting.add(Settings(
                SettingTag.DEFAULT_BROWSER,
                getString(R.string.useDefaultBrowser),
                R.drawable.icons8_globe,
                R.color.red2,
                true
            ))
        }

        return setting.filter { it.isListed }.toMutableList()
    }

    fun Fragment.appearance(): MutableList<Settings> {
        val setting = mutableListOf<Settings>()

        safeContext {
            val currentLanguage = listOfLanguage().find { it.value == prefs(Constant.SharedPrefs.CURRENT_LOCALE) }

            setting.add(Settings(
                SettingTag.LANGUAGE,
                getString(R.string.language),
                R.drawable.icons8_language_4,
                R.color.cardBackground,
                false,
                currentLanguage?.title
            ))

            setting.add(Settings(
                SettingTag.THEME,
                getString(R.string.dark_mode),
                R.drawable.icons8_crescent_moon,
                com.acxdev.shimmer.R.color.black,
                true,
            ))

            val prefStyle = getSavedPrefs(Constant.SharedPrefs.COLOR)
            val currentStyle = listOfStyle().find { it.title == prefStyle }

            setting.add(Settings(
                SettingTag.COLOR,
                getString(R.string.color),
                R.drawable.icons8_paint_palette,
                currentStyle?.value.toString().toInt(),
                false,
                prefStyle.toString()
            ))
        }

        return setting.filter { it.isListed }.toMutableList()
    }

    fun Fragment.notification(): MutableList<Settings> {
        val setting = mutableListOf<Settings>()

        safeContext {
            setting.add(Settings(
                SettingTag.ALLOW_NOTIFICATION,
                getString(R.string.allowNotifications),
                R.drawable.icons8_notification_center_1,
                R.color.purple,
                true
            ))

            setting.add(Settings(
                SettingTag.REPEAT,
                getString(R.string.refreshPeriod),
                R.drawable.icons8_rollback,
                R.color.yellow,
                false,
                listOfRefreshPeriod().find { it.value == prefs(Constant.SharedPrefs.REFRESH_PERIOD).toLong() }?.title
            ))

            setting.add(Settings(
                SettingTag.WORKER_OFFLINE,
                getString(R.string.workerOffline),
                R.drawable.icons8_gpu_off,
                R.color.red,
                true
            ))

            setting.add(Settings(
                SettingTag.HASHRATE_DROP,
                getString(R.string.hashrateDrop),
                R.drawable.icons8_activity,
                R.color.redSecondary,
                false,
                prefs(Constant.SharedPrefs.HASHRATE_DROP).toFloat().toPercent(),
                isListed = false
            ))

            setting.add(Settings(
                SettingTag.PAYMENTS,
                getString(R.string.payments),
                R.drawable.icons8_stack_of_coins_2,
                R.color.telegramBackground,
                true
            ))
        }

        return setting.filter { it.isListed }.toMutableList()
    }

    fun Fragment.support(): MutableList<Settings> {
        val setting = mutableListOf<Settings>()

        safeContext {
            setting.add(Settings(
                SettingTag.SHARE,
                getString(R.string.shareApps),
                R.drawable.icons8_share_3,
                R.color.green2,
                false,
                isListed = false
            ))

            setting.add(Settings(
                SettingTag.RATE,
                getString(R.string.rateUs),
                R.drawable.icons8_star,
                R.color.yellow,
                false,
                isListed = false
            ))

            setting.add(Settings(
                SettingTag.HELP_US_TRANSLATE,
                getString(R.string.helpUsTranslate),
                R.drawable.icons8_translation,
                R.color.yellow2,
                false
            ))

            setting.add(Settings(
                SettingTag.WHATS_NEW,
                getString(R.string.whatsNew),
                R.drawable.icons8_features_list,
                R.color.primaryBlue
            ))

            setting.add(Settings(
                SettingTag.ABOUT,
                getString(R.string.about),
                R.drawable.ic_logo,
                com.acxdev.commonFunction.R.color.white,
                false,
                "${getString(R.string.version)} ${getVersionName()}"
            ))
        }

        return setting.filter { it.isListed }.toMutableList()
    }

    fun Fragment.joinCommunity(): MutableList<Settings> {
        val setting = mutableListOf<Settings>()

        safeContext {
            setting.add(Settings(
                SettingTag.FACEBOOK,
                "Facebook",
                R.drawable.icons8_facebook_logo,
                R.color.facebook_background,
                isListed = false
            ))

            setting.add(Settings(
                SettingTag.DISCORD,
                "Discord",
                R.drawable.icons8_discord,
                R.color.discordBackground
            ))

            setting.add(Settings(
                SettingTag.TELEGRAM,
                "Telegram",
                R.drawable.icons8_telegram_app_1,
                R.color.telegramBackground,
                isListed = false
            ))
        }

        return setting.filter { it.isListed }.toMutableList()
    }
}