package com.acxdev.poolguardapps.util

import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.poolguardapps.repository.Crypto
import java.text.DecimalFormat

fun zeroCrypto(symbol: String?): String = "${ConstantLib.ZERO} ${symbol!!.uppercase()}"

fun Any?.toCrypto(symbol: String?, cryptoFormat: CryptoFormat): String {
    val format = DecimalFormat("0.#####")
    return when (cryptoFormat) {
        CryptoFormat.FORMAT_SYMBOL -> {
            try {
                "${format.format(toString().toBigDecimal())} ${symbol!!.uppercase()}"
            } catch (e: Exception) {
                "${ConstantLib.ZERO} ${symbol!!.uppercase()}"
            }
        }
        CryptoFormat.DIVIDE_BLOCK_SYMBOL -> {
            try {
                "${
                    format.format(
                        toString().toBigDecimal().divide(symbol!!.crypto().block.toBigDecimal())
                    )
                } ${symbol.uppercase()}"
            } catch (e: Exception) {
                "${ConstantLib.ZERO} ${symbol!!.uppercase()}"
            }
        }
        CryptoFormat.DIVIDE_BLOCK -> {
            try {
                toString().toBigDecimal().divide(symbol!!.crypto().block.toBigDecimal()).toString()
            } catch (e: Exception) {
                ConstantLib.ZERO
            }
        }
        CryptoFormat.MULTIPLY_BLOCK -> {
            try {
                toString().toBigDecimal().multiply(symbol!!.crypto().block.toBigDecimal())
                    .toString()
            } catch (e: Exception) {
                ConstantLib.ZERO
            }
        }
    }
}

fun Double?.invalidateSoloPool(symbol: String): Double {
    return try {
        when (symbol) {
            Crypto.BEAM.name -> this!! / 10
            Crypto.BTG.name -> this!! / 10
            Crypto.ETC.name -> this!! * 1000000000
            Crypto.ETHW.name -> this!! * 1000000000
            Crypto.FIRO.name -> this!! / 10
            Crypto.FLUX.name -> this!! / 10
            Crypto.KMD.name -> this!! / 1000000000
            Crypto.LTC.name -> this!! / 1000000000
            Crypto.RVN.name -> this!! / 10
            Crypto.XMR.name -> this!! * 1000
            Crypto.ZEC.name -> this!! / 10
            Crypto.ZEN.name -> this!! / 1000000000
            else -> this!!
        }
    } catch (e: Exception) {
        0.0
    }
}

fun Double?.invalidateK1Pool(symbol: String): Double {
    return try {
        when (symbol) {
            Crypto.CLO.name -> this!! / 1000000000
            Crypto.EXP.name -> this!! / 1000000000
            Crypto.ETHW.name -> this!! / 1000000000
            Crypto.ETC.name -> this!! / 1000000000
            else -> this!!
        }
    } catch (e: Exception) {
        0.0
    }
}

fun Double?.invalidateBaikalMine(symbol: String): Double {
    return try {
        when (symbol) {
            Crypto.CLO.name -> this!!
            Crypto.EXP.name -> this!! / 1000000000
            Crypto.ETHW.name -> this!! * 1000000000
            Crypto.ETC.name -> this!! * 1000000000
            else -> this!!
        }
    } catch (e: Exception) {
        0.0
    }
}

fun Double?.invalidateMyMiners(symbol: String): Double {
    return try {
        when (symbol) {
            Crypto.EXP.name -> this!! / 1000000000
            Crypto.ETHW.name -> this!! * 1000000000
            Crypto.ETC.name -> this!! * 1000000000
            else -> this!!
        }
    } catch (e: Exception) {
        0.0
    }
}