package com.acxdev.poolguardapps.model.pool_response

data class MinerpoolResponse(val miner: String, val hashAvg5m: Float, val hashAvg24h: Float, val hashAvg7d: Float, val paymentsAssetsPending: Double, val customPayout: String,
                             val lpUptime: String, val lpRounds: String, val lpRounds7d: String, val lpMinTimeNeeded: String, val totalHash: Double, val totalShares: Double,
                             val luckDays: String, val luckHours: String, val networkSols: String, val immature: Double, val balance: Double, val paid: Double,
                             val workers: Map<String, MinerpoolWorker>, val history: List<MinerpoolHistory>)

data class MinerpoolHistory(val t: Long, val h: Double, val havg2h: Double)

data class MinerpoolWorker(val name: String, val diff: Long, val shares: Double, val invalidshares: Double, val currRoundShares: Double, val currRoundTime: Long, val hashrate: Double,
                            val hashrateString: String, val luckDays: String, val luckHours: String, val paid: Double, val balance: Double)