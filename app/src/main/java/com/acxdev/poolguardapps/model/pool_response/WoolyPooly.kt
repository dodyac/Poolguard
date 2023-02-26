package com.acxdev.poolguardapps.model.pool_response

data class BalanceMovement(val blockheight: Long, val amount: Double, val participation: Double, val type: Type, val created: String, val confirmationprogress: Double)

enum class Type { Immature, Reward }

data class BlockRate(val pplns: List<BlockRatePpln>, val solo: List<BlockRatePpln>)

data class BlockRatePpln(val created: String, val total_count: Long, val normal_count: Long, val orhpan_count: Long, val uncle_count: Long, val effort: Double)

data class MatureBlock(val height: Long, val hash: String, val timestamp: Long, val effort: Double, val uncle: Boolean,
                       val confirmationprogress: Double, val miner: String, val mode: String, val algo: Algo, val orphan: Boolean, val reward: Double)

enum class Algo { Default }

data class ModeStats(val pplns: Pplns, val solo: Pplns)

data class Pplns(val default: Default)

data class Default(val currentHashrate: Float, val hashrate: Float, val dayHashrate: Float, val totalEffort: Double)

data class Perfomance(val pplns: List<PerfomancePpln>, val solo: List<PerfomancePpln>)

data class PerfomancePpln(val algo: Algo, val created: String, val hashrate: Float)

data class WoolyPoolyStats(val balance: Double, val immature_balance: Double, val paid: Double, val todayPaid: Double, val income: Income)

data class Income(val income_Hour: Double, val income_HalfDay: Double, val income_Day: Double, val income_Week: Double, val income_Month: Double)

data class WoolyPoolyResponse(val mode_stats: ModeStats, val stats: WoolyPoolyStats, val payments: List<MinerPayment>, val workers: List<MinerWorker>,
                              val workersTotal: Long, val workersOnline: Long, val workersOffline: Long, val balance_movements: List<BalanceMovement>,
                              val block_rate: BlockRate, val mature_blocks: List<MatureBlock>, val immature_blocks: List<Any?>, val mature_count: Long,
                              val immature_count: Long, val perfomance: Perfomance)
