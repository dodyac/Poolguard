package com.acxdev.poolguardapps.model.pool_response

data class Dashboard(val status: String, val data: Data)

data class Data(val statistics: List<CurrentStatistics>, val workers: List<CurrentStatistics>, val currentStatistics: CurrentStatistics, val settings: Settings, val credits: Credits)

data class CurrentStatistics(val time: Long? = null, val lastSeen: Long? = null, val reportedHashrate: Float? = null, val currentHashrate: Float? = null,
                             val averageHashrate: Float? = null, val validShares: Float? = null, val coinsPerMin: Double? = null, val usdPerMin: Double? = null,
                             val btcPerMin: Double? = null, val invalidShares: Float? = null, val staleShares: Float? = null, val activeWorkers: Long? = null,
                             val unpaid: Double? = null, val worker: String? = null, val unconfirmed: Double? = null)

data class Settings(val email: String, val monitor: Long, val minPayout: Long, val method: Int, val ip: String)

data class CurrentStats(val status: String, val data: CurrentStatistics?)

data class SettingsParent(val status: String, val data: Settings)

data class History(val status: String, val data: List<CurrentStatistics>, val error: Any? = null, val result: MutableList<Result>)

data class Workers(val status: String, val data: MutableList<CurrentStatistics>)

data class Payout(val status: String, val data: MutableList<PayoutData>)

data class PayoutData(val amount: Double, val txHash: String? = null, val paidOn: Long, val kernelId: String? = null, val start: Long? = null, val end: Long? = null)

data class Credits(val miner: String, val credit: Double, val time: String, val balance: Double, val maxCredit: Double)