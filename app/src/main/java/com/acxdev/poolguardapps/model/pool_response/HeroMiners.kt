package com.acxdev.poolguardapps.model.pool_response

data class HeroMinersResponse(val stats: HeroMinersStats, val payments: List<String>, val charts: HeroMinersChart, val workers: List<HeroMinersWorker>,
                              val unlocked: List<String>, val unlocked_daily: List<String>, val unconfirmed: List<HeroMinersUnconfirmed>?)

data class HeroMinersStats(val donation_level: String, val shares_good: Float, val hashes: String, val lastShare: String, val balance: String, val shares_stale: Float,
                           val paid: String, val hashrate: Float, val roundScore: Long, val roundHashes: Long, val poolRoundScore: Long, val poolRoundHashes: Long,
                           val networkHeight: Long, val hashrate_1h: Double, val hashrate_6h: Float, val hashrate_24h: Float, val shares_invalid: Float,
                           val solo_shares_good: Long, val solo_shares_invalid: Long, val solo_shares_stale: Long, val soloRoundHashes: Long, val payments_24h: Double,
                           val payments_7d: Double)

data class HeroMinersUnconfirmed(val height: Long, val hash: String, val time: String, val difficulty: String, val totalShares: String, val shares: String,
                                 val totalScore: String, val score: String, val reward: String, val nonce: String, val blockReward: String, val status: String,
                                 val region: String, val rewardScheme: String, val totalSharesAdj: String)

data class HeroMinersWorker(val name: String, val hashrate: Float, val region: String, val port: String, val agent: String, val stratum: String, val solo: String,
                            val lastShare: Long, val shares_good: Float, val shares_invalid: Float, val shares_stale: Float, val lastJobDifficulty: Long, val hashes: String,
                            val solo_hashes: Long, val hashesSinceBlock: String, val hashrate_1h: Float, val hashrate_6h: Float, val hashrate_24h: Float)

data class HeroMinersChart(val payments: List<List<Double>>, val hashrate: List<List<Float>>)