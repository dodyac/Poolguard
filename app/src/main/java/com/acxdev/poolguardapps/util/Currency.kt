package com.acxdev.poolguardapps.util

import android.content.Context
import com.acxdev.poolguardapps.common.Constant
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun listOfCurrency() = listOf(
    Locale.US,
    Locale("id", "ID"),
    Locale("en", "IE"),
    Locale("hi", "IN"),
    Locale("es", "AR"),
    Locale("en", "AU"),
    Locale("pt", "BR"),
    Locale.CANADA,
    Locale.UK,
    Locale.KOREA,
    Locale("ms", "MY"),
    Locale("tr", "TR"),
    Locale("pl", "PL"),
    Locale.CHINA,
    Locale.JAPAN,
    Locale("es", "MX"),
    Locale("es", "PE"),
    Locale("ro", "RO"),
    Locale("ru", "RU"),
    Locale("en", "SG"),
    Locale("th", "TH"),
    Locale.TAIWAN,
    Locale("bn", "BD"),
    Locale("km", "KH"),
    Locale("en", "HK"),
    Locale("si", "LK"),
    Locale("vi", "VN"),
    Locale("ms", "BN"),
    Locale("bg", "BG"),
    Locale("es", "CO"),
    Locale("es", "CR"),
    Locale("en", "PH"),
    Locale("en", "SE"),
    Locale("da", "DK"),
    Locale("is", "IS"),
    Locale("en", "JM"),
    Locale("ne", "NP"),
    Locale("no", "NO"),
    Locale("en", "PK"),
    Locale("de", "CH"),
    Locale("hr", "HR"),
    Locale("sr", "RS"),
    Locale("en", "ZA"),
    Locale("fr", "TN"),
    Locale("cs", "CZ"),
    Locale("am", "ET"),
    Locale("hu", "HU"),
    Locale("kk", "KZ"),
    Locale("en", "KE"),
    Locale("es", "UY"),
    Locale("ro", "MD"),
    Locale("en", "TZ"),
    Locale("hy", "AM"),
    Locale("es", "PY"),
    Locale("es", "CL"),
    Locale("ru", "BY"),
    Locale("en", "BW"),
    Locale("en", "AE"),
    Locale("pt", "AO"),
    Locale("nl", "AW"),
    Locale("uk", "UA"),
    Locale("ka", "GE"),
    Locale("en", "GH"),
    Locale("bs", "BA"),
    Locale("en", "NG"),
    Locale("ar", "QA"),
    Locale("ky","KG"),
    Locale("ar","KW"),
    Locale("ar","OM"),
    Locale("es","PA"),
    Locale("lg","UG"),
).sortedBy { Currency.getInstance(it).getDisplayName(Locale.ENGLISH) }

fun Any.toLocalCurrency(context: Context, minimumFractionDigits: Int = 2): String {
    val locale = listOfCurrency().find {
        Currency.getInstance(it).currencyCode == context.getSavedPrefs(Constant.SharedPrefs.CURRENCY)
    }!!

    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.minimumFractionDigits = minimumFractionDigits

    return numberFormat.format(this)
}

fun Any.calculateLocalCurrency(currency: Double, context: Context): String {
    return toString().toBigDecimal().multiply(currency.toBigDecimal()).toLocalCurrency(context)
}

fun Any.multiplyLocalCurrency(currency: Double): BigDecimal =
    toString().toBigDecimal().multiply(currency.toBigDecimal())

fun Context.zeroLocalCurrency(): String = 0.toLocalCurrency(this)
