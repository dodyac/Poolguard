package com.acxdev.poolguardapps.model.pool_response

data class Overview(val hashrate: Float, val hashrate24h: Float, val onlineWorkerCount: String, val reportedHashrate: Float, val reportedHashrate24h: Float,
                    val sharesStatusStats: SharesStatusStats)

data class SharesStatusStats(val lastShareDt: String, val staleCount: Float, val staleRate: Float, val validCount: Float, val validRate: Float)

data class Billing(val earningStats: List<EarningStat>, val expectedReward24H: Double, val expectedRewardWeek: Double, val pendingPayouts: List<SucceedPayout>,
                   val succeedPayouts: List<SucceedPayout>, val totalPaid: Double, val totalUnpaid: Double)

data class EarningStat(val meanReward: Double, val reward: Double, val timestamp: String)

data class SucceedPayout(val amount: Double, val approveUUID: String, val coin: String, val createdAt: String, val meta: String, val status: String, val txHash: String,
                         val txMeta: String, val type: String, val updatedAt: String, val userAddress: String, val uuid: String)

data class HashrateResponse(val items: List<HashrateHiveOn>, var shares: SharesResponse)

data class HashrateHiveOn(val hashrate: Float, val meanHashrate: Float, val reportedHashrate: Float, val timestamp: String)

data class PayoutHiveOn(val items: List<SucceedPayout>, val total: String)

data class SharesResponse(val items: List<SharesHiveOn>)

data class SharesHiveOn(val lastShareDt: String, val staleCount: Float? = null, val staleRate: Float? = null, val timestamp: String, val validCount: Float, val validRate: Float)

data class WorkerResponse(val workers: Map<String, WorkersHiveOn>)

data class WorkersHiveOn(val hashrate: Float, val hashrate24h: Float, val sharesStatusStats: SharesStatusStats, val online: Boolean?)