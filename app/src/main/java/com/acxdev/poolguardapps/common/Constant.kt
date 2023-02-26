package com.acxdev.poolguardapps.common

import okhttp3.logging.HttpLoggingInterceptor

object Constant {

//    val INTERCEPTOR_TYPE = HttpLoggingInterceptor.Level.BODY
    val INTERCEPTOR_TYPE = HttpLoggingInterceptor.Level.BASIC
    const val TAG_DIALOG = "dialogProgress"

    var SELECTED_COIN = -1
    const val YEAR = "yyyy"
    const val MONTH = "MMM"
    const val PATTERN_DATE = "EEE, dd MMMM yyyy"
    const val COLON = ":"

    const val CHANNEL_ID_WORKER_OFFLINE = "channel-01"
    const val CHANNEL_ID_PAYMENT_NOTIFICATION = "channel-02"
    const val BACKGROUND_PROCESS = "BackgroundProcess"
    const val CHANNEL_ID_ANNOUNCEMENT ="channel-03"
    const val ANNOUNCEMENT = "Announcement"
    const val ANNOUNCEMENT_DESCRIPTION = "Announcement_description"
    const val TOPIC_ALL = "smk"
    const val HOUR_MINUTE = "HH:mm"

    object Intent {
        const val ID = "_id"
        const val POOL = "pool"
        const val CURRENT_POSITION = "currentPosition"
        const val GPU = "gpu"
    }

    object SharedPrefs {
        const val CURRENCY = "currency"
        const val REFRESH_PERIOD = "refresh_period"
        const val AUTO_CHECK = "auto_check"
        const val WORKER_OFFLINE = "Worker Offline"
        const val PAYMENT_NOTIFICATION = "Payment Notification"
        const val HASHRATE_DROP = "hashrate_drop"
        const val SCREEN_DEFAULT = "screenDefault"
        const val IS_WALLET_SCREEN = "walletScreenDefault"
        const val IS_FIAT = "isFiat"
        const val CALCULATOR_STATE = "calculatorState"
        const val DIFFICULTY_STATE = "diffState"
        const val BLOCK_REWARD_STATE = "blockRewardState"
        const val ALGORITHM_STATE = "algState"
        const val POWER_COST_GPU_STATE = "pcgState"
        const val PINNED_PROFITABILITY_STATE = "pinnedState"
        const val HASHRATE_STATE = "hashState"
        const val COIN_STATE = "coinState"
        const val POWER_STATE = "powerState"
        const val COST_STATE = "costState"
        const val FEE_STATE = "feeState"
        const val UNIT_STATE = "unitState"
        const val CURRENT_LOCALE = "curLang"
        const val IS_OPEN_DEFAULT_BROWSER = "isOpenDefaultBrowser"
        const val CURRENT_NETWORK = "curNet"
        const val WHATS_NEW = "whats_new"
        const val COLOR = "color"
        const val IS_FIRST_TIME = "isFirstTime"
    }

    object DefaultValue {
        const val USD = "USD"
        const val OPTION_MENU = 3
        const val MAX_FLEX = 5
        const val MAX_NEWS = 5
        const val MAX_PAYMENTS = 2
        const val MAX_WORKERS = 4
        const val FONT_SCALE = 1.0F
        const val SIZE_HD = 275
        const val SIZE_FHD = 430
        const val DELAY_TAP_TO_EXIT = 2000L
        const val DELAY_UPDATE_VALUE = 1000L
        const val DELAY_MAX_LOADING = 30000L
        const val DURATION_AUTO_CHECK = 60
        const val DURATION_DELETE_WALLET = 6
        const val RESULT_WALLET_ADDED = 71
        const val RESULT_WALLET_DELETED = 19
        const val RESULT_MIGRATE_WALLET = 22
    }
}