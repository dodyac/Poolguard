package com.acxdev.poolguardapps.util

import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.poolguardapps.repository.Crypto
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

//TODO Raw String
fun Any?.formatReadable(isHashrate: Boolean = true): String {
    val baseSuffix = if (isHashrate) {
        "H/s"
    } else {
        "B"
    }

    val arraySuffix = if (isHashrate) {
        arrayOf(
            emptyString(),
            " K",
            " M",
            " G",
            " T",
            " P",
            " E",
            " Z",
            " Y",
            " Geop"
        )
    } else {
        arrayOf(
            emptyString(),
            " Ki",
            " Mi",
            " Gi",
            " Ti",
            " Pi",
            " Ei",
            " Zi",
            " Yi",
            " Geop"
        )
    }

    return try {
        val x = toString().toBigDecimal().toPlainString().split(".")
        val numValue = x[0].toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        if (value >= 3 && base < arraySuffix.size) {
            DecimalFormat("0.##")
                .format(numValue / 10.0.pow((base * 3).toDouble())) + arraySuffix[base] + baseSuffix
        } else {
            DecimalFormat("0.##").format(this) + baseSuffix
        }
    } catch (e: Exception) {
        "0 $baseSuffix"
    }
}

fun Any?.fixHashRate(symbol: String): Float {
    return try {
        when (symbol) {
            Crypto.ZEC.name -> toString().toFloat()
            Crypto.XMR.name -> toString().toFloat()
            else -> (toString().toFloat() * 1000000F)
        }
    } catch (e: Exception) {
        0.0F
    }
}

fun Any?.fixHashRateUnMineable(symbol: String): Float {
    return try {
        when (symbol) {
            Crypto.XMR.name -> toString().toFloat()
            else -> (toString().toFloat() * 1000000F)
        }
    } catch (e: Exception) {
        0.0F
    }
}

fun Any?.invalidateFormat(symbol: String): String = formatReadable(symbol != Crypto.XCH.name)