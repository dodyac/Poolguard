package com.acxdev.poolguardapps.model

data class Settings(
    val tags: SettingTag,
    val title: String,
    val icon: Int,
    val iconBackground: Int,
    val isToggle: Boolean = false,
    val subtitle: String? = null,
    val isListed: Boolean = true
)

enum class SettingTag {
    ACCOUNT,
    CURRENCY,
    DEFAULT_SCREEN,
    DEFAULT_BROWSER,
    CHECK_FOR_UPDATE,
    ADVANCED,

    LANGUAGE,
    THEME,
    COLOR,

    ALLOW_NOTIFICATION,
    REPEAT,
    WORKER_OFFLINE,
    HASHRATE_DROP,
    PAYMENTS,

    FACEBOOK,
    DISCORD,
    TELEGRAM,

    SHARE,
    RATE,
    HELP_US_TRANSLATE,
    WHATS_NEW,
    ABOUT,

    SYNC,
    RESTORE,
    BUY,
    SIGN_OUT,
    DEVICES,
}