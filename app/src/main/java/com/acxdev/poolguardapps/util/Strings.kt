package com.acxdev.poolguardapps.util

import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.poolguardapps.model.Wallet

fun String.removeSuffixPool() = removeSuffix(" SOLO")
    .removeSuffix(" RBPPS+")
    .removeSuffix(" PPS+")
    .removeSuffix(" PPS")
    .removeSuffix(" Dual Mining (+ ZIL)")

fun String.removeCfx() = replace("cfx:", emptyString())

fun String.remove0x() = lowercase().replace("0x", emptyString())

fun String.removeAPI() = replace("api.", emptyString())
    .replace("api-", emptyString())
    .replace("api", emptyString())
    .replace("v2/", emptyString())
    .replace("v1/", emptyString())

fun String.removePrefixAddress(wallet: Wallet) = removePrefix(wallet.address + ".")