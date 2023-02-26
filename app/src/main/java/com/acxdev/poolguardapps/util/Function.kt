package com.acxdev.poolguardapps.util

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.commonFunction.util.ext.getCompatActivity
import com.acxdev.commonFunction.util.ext.getVersionName
import com.acxdev.commonFunction.util.ext.toDate
import com.acxdev.commonFunction.util.ext.toDateEpoch
import com.acxdev.commonFunction.util.ext.view.setImageUrl
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.repository.MyAlgorithm
import com.acxdev.poolguardapps.repository.Pool
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.model.Reward
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.rest.fetchApi
import com.acxdev.poolguardapps.rest.onLoaded
import com.acxdev.poolguardapps.ui.main.ActivityMain
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun Context.whatToMine(symbol: String, hashRate: Float?, progress: Boolean = false, reward: Reward.() -> Unit){
    fetchApi(BaseUrl.URL_WHATTOMINE).coinStats(symbol.crypto().whatToMineCode).onLoaded(this, progress) {
        val rewardSecondly = hashRate.calculate(nethash, block_time, block_reward)
        val rewardMinutely = rewardSecondly * 60
        val rewardHourly = rewardMinutely * 60
        val rewardDaily = rewardHourly * 24
        val rewardWeekly = rewardDaily * 7
        val rewardMonthly = rewardDaily * 30
        val rewardYearly = rewardDaily * 365
        val whattomine = this
        fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(symbol, getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()).onLoaded(this@whatToMine, progress) {
            try {
                val price = this[getSavedPrefs(Constant.SharedPrefs.CURRENCY)]!!
                reward.invoke(Reward(price, rewardSecondly, rewardMinutely, rewardHourly, rewardDaily, rewardWeekly, rewardMonthly, rewardYearly, whattomine))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun Float?.calculate(NetHashesPerSecond: Double, BlockTime: Double, BlockReward: Double): Double{
    val profitabilityToMineNextBlock = try { this!! / NetHashesPerSecond } catch (e: NullPointerException){ 0.0 }
    val blockInSecond = 1 / BlockTime
    val blockMinedPerSecond = profitabilityToMineNextBlock * blockInSecond
    return (BlockReward * blockMinedPerSecond)
}

fun Long?.toTimesAgo(context: Context, isEpoch: Boolean = true): String {
    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS

    if(this != null){
        var time = Date(this).time
        if (time < 1000000000000L) time *= 1000

        val now = Calendar.getInstance().time.time
        if (time > now || time <= 0) return context.getString(R.string.inTheFuture)

        val diff = now - time

        return when {
            diff < MINUTE_MILLIS -> context.getString(R.string.momentsAgo)
            diff < 2 * MINUTE_MILLIS -> context.getString(R.string.aMinuteAgo)
            diff < 60 * MINUTE_MILLIS -> context.getString(R.string.minutesAgo, (diff / MINUTE_MILLIS))
            diff < 2 * HOUR_MILLIS -> context.getString(R.string.anHourAgo)
            diff < 24 * HOUR_MILLIS -> context.getString(R.string.hoursAgo, (diff / HOUR_MILLIS))
            diff < 48 * HOUR_MILLIS -> context.getString(R.string.yesterday)
            (diff / DAY_MILLIS) <= 7 -> context.getString(R.string.daysAgo, (diff / DAY_MILLIS))
            else -> {
                if(isEpoch){
                    toDateEpoch(
                        Constant.PATTERN_DATE,
                        Locale(context.getSavedPrefs(Constant.SharedPrefs.CURRENT_LOCALE).toString())
                    )
                } else {
                    toDate(
                        Constant.PATTERN_DATE,
                        Locale(context.getSavedPrefs(Constant.SharedPrefs.CURRENT_LOCALE).toString())
                    )
                }
            }
        }
    } else return "-"
}

fun String.toEpoch(): Long {
    return try {
        //TODO Raw String
        val myDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        myDate.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond / 1000
    } catch (e: Exception) {
        0L
    }
}

fun String.toEpochHiveOn(): Long {
    return try {
        //TODO Raw String
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        val date = formatter.parse(this)!!
        date.time / 1000
    } catch (e: Exception){
        e.printStackTrace()
        0L
    }
}

fun String.toEpochHiveOnChart(): Long {
    return try {
        //TODO Raw String
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val date = formatter.parse(this)!!
        date.time / 1000
    } catch (e: Exception){
        e.printStackTrace()
        0L
    }
}

fun Context.restartApps() {
    Intent(this, ActivityMain::class.java)
        .also {
            startActivity(it)
            getCompatActivity().finishAffinity()
        }
}

fun Context.chiaFormula(spaceRate: Float?, reward: Reward.() -> Unit) {
    fetchApi(BaseUrl.URL_CHIA_NETWORK).chiaNetspace().onLoaded(this) {
        val rewardDaily = try { (spaceRate!! / netspace) * 4608 * 2 } catch (e: Exception){ 0.0 }
        val rewardHourly = rewardDaily / 24
        val rewardMinutely = rewardHourly / 60
        val rewardSecondly = rewardMinutely / 60
        val rewardWeekly = rewardDaily * 7
        val rewardMonthly = rewardDaily * 30
        val rewardYearly = rewardDaily * 365
        fetchApi(BaseUrl.URL_CRYPTOCOMPARE).getCryptoCompare(Crypto.XCH.name, getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()).onLoaded(this@chiaFormula) {
            val price = this[getSavedPrefs(Constant.SharedPrefs.CURRENCY)]!!
            reward.invoke(Reward(price, rewardSecondly, rewardMinutely, rewardHourly, rewardDaily, rewardWeekly, rewardMonthly, rewardYearly))
        }
    }
}

fun Context.getSavedPrefs(path: String): Any? {
    //TODO Raw String
    return when(path){
        Constant.SharedPrefs.CURRENCY -> getPrefs().getString(path, Constant.DefaultValue.USD)
        Constant.SharedPrefs.IS_FIRST_TIME -> getPrefs().getString(path, emptyString())
        ConstantLib.IS_DARK_MODE -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.REFRESH_PERIOD -> getPrefs().getLong(path,3600000L)
        Constant.SharedPrefs.AUTO_CHECK -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.WORKER_OFFLINE -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.PAYMENT_NOTIFICATION -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.HASHRATE_DROP -> getPrefs().getFloat(path,30F)
        Constant.SharedPrefs.SCREEN_DEFAULT -> getPrefs().getInt(path,0)
        Constant.SharedPrefs.IS_WALLET_SCREEN -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.IS_FIAT -> getPrefs().getBoolean(path,true)
        Constant.SharedPrefs.CALCULATOR_STATE -> getPrefs().getInt(path,1)
        Constant.SharedPrefs.DIFFICULTY_STATE -> getPrefs().getString(path, "24h")
        Constant.SharedPrefs.BLOCK_REWARD_STATE -> getPrefs().getString(path, "24h")
        Constant.SharedPrefs.ALGORITHM_STATE -> getPrefs().getString(path, MyAlgorithm.Ethash.name)
        Constant.SharedPrefs.POWER_COST_GPU_STATE -> getPrefs().getString(path, "0.12")
        Constant.SharedPrefs.PINNED_PROFITABILITY_STATE -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.HASHRATE_STATE -> getPrefs().getString(path, emptyString())
        Constant.SharedPrefs.POWER_STATE -> getPrefs().getString(path, emptyString())
        Constant.SharedPrefs.COST_STATE -> getPrefs().getString(path, emptyString())
        Constant.SharedPrefs.FEE_STATE -> getPrefs().getString(path, emptyString())
        Constant.SharedPrefs.UNIT_STATE -> getPrefs().getString(path, "MH/s")
        Constant.SharedPrefs.COIN_STATE -> getPrefs().getString(path, Crypto.ETHW.name)
        Constant.SharedPrefs.CURRENT_LOCALE -> getPrefs().getString(path, "en")
        Constant.SharedPrefs.CURRENT_NETWORK -> getPrefs().getString(path, "ERC20")
        Constant.SharedPrefs.IS_OPEN_DEFAULT_BROWSER -> getPrefs().getBoolean(path,false)
        Constant.SharedPrefs.WHATS_NEW -> getPrefs().getString(getVersionName(), emptyString())
        Constant.SharedPrefs.COLOR -> getPrefs().getString(path, "Crayola")
        else -> null
    }
}

fun Wallet.pool(): Pool = try {
    Pool.values().toList().find { it.value == pool }!!
} catch (e: Exception) {
    Pool.ETHERMINE
}

fun String.crypto(): Crypto = try {
    Crypto.values().toList().find { it.name == this }!!
} catch (e: Exception) {
    Crypto.ETHW
}

fun Wallet.getBaseUrl(): String = pool().coin.find { it.crypto.name == symbol }?.baseUrl.toString()

fun Context.currentTheme(): Int {
    return when(getSavedPrefs(Constant.SharedPrefs.COLOR)) {
        getString(R.string.electricPink) -> R.style.Theme_PoolguardRed
        getString(R.string.limeGreen) -> R.style.Theme_PoolguardGreen
        getString(R.string.vividGamboge) -> R.style.Theme_PoolguardYellow
        getString(R.string.blueCola) -> R.style.Theme_PoolguardBlue2
        getString(R.string.ufoGreen) -> R.style.Theme_PoolguardGreen2
        getString(R.string.bittersweet) -> R.style.Theme_PoolguardRed2
        getString(R.string.spanishYellow) -> R.style.Theme_PoolguardYellow2
        getString(R.string.iris) -> R.style.Theme_PoolguardPurple
        getString(R.string.midNightAmoled) -> R.style.Theme_PoolguardAmoled
        else -> R.style.Theme_Poolguard
    }
}

fun ImageView.imageUrl(url: String) {
    setImageUrl(url, errorDrawable = R.drawable.ic_logo)
}