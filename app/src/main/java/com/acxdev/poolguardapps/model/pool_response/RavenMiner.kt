package com.acxdev.poolguardapps.model.pool_response

import com.google.gson.annotations.SerializedName

data class RavenMiner(val minPayout: Long, val balance: Balance, val earnings: Map<String, Double>, val earningsPPLNS: List<EarningsPPLN>, val payouts: List<PayoutRavenMiner>,
                      val workers: WorkersRavenMiner, val hashrate: Hashrate)

data class Balance(val pending: Double, val cleared: Double)

data class EarningsPPLN(val amount: Double, val time: Long, val status: Long, val blockid: Double, val type: String, val height: Long? = null, val confirmations: Long? = null)

data class Stats24H(val t: String, val hr: Float)

data class PayoutRavenMiner(val time: Long, val amount: Double, val tx: String)

data class WorkersRavenMiner(val list: List<WorkersRavenMinerList>, val count: Long, val onlineCount: Long, val offlineCount: Long)

data class WorkersRavenMinerList(val type: String, val name: String, val hr5m: Float, val lastShareTime: Long, val connectedTime: Long, val updated: Long,
                                 val secondsSinceLastActivity: Long, val status: Long, val difficulty: Double, val sharesperminute: Long, val online: Any)
data class Hashrate(
    @SerializedName("5min")
    val the5Min: Float,
    @SerializedName("1h")
    val the1H: Float,
    @SerializedName("6h")
    val the6H: Float,
    @SerializedName("12h")
    val the12H: Float,
    @SerializedName("24h")
    val the24H: Float,
    @SerializedName("stats24h")
    val stats24H: List<Stats24H>)