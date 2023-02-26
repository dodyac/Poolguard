package com.acxdev.poolguardapps.model.pool_response

data class RavenPool(val coinTicker: String, val lastBlockReward: String, val mineraddress: String, val totalHash: Float, val totalShares: Double, val totalPoolShares: Double,
                     val totalPoolHash: Double, val networkHash: String, val networkDiff: String, val immature: Double, val balance: Double, val paid: Double,
                     val miner: Map<String, MinerRavenPool>, val workers: Map<String, WorkersRavenPool>, val historyminer: Map<String, Historyminer>, val historyworkers: Map<String, List<Historyminer>>)

data class Historyminer(val time: Long, val hashrate: Float)

data class MinerRavenPool(val lastShare: Long, val name: String, val shares: Float, val invalidshares: Float, val currRoundShares: Float, val hashrate: Float,
                          val hashrateString: String, val luckDays: String, val luckHours: String)

data class WorkersRavenPool(val lastShare: Long, val name: String, val diff: Long, val shares: Float, val invalidshares: Float, val currRoundShares: Float, val hashrate: Float,
                            val hashrateString: String, val luckDays: String, val luckHours: String, val balance: Double, val immature: Double, val paid: Double)