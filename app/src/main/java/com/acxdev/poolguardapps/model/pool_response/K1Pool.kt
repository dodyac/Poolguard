package com.acxdev.poolguardapps.model.pool_response

data class K1PoolResponse(val miner: K1PoolMiner, val pool: K1Pool)

data class K1Pool(val now: Long, val nShares: Float, val minersTotal: Long, val maturedTotal: Float, val poolHashrate: Float, val poolHashrateStr: String, val poolLuck: Double,
                  val networkDiff: Float, val blocksCandidatesTotal: Float, val blocksImmatureTotal: Float, val blocksMaturedTotal: Float, val networkBlock: Float,
                  val networkEpoch: Long, val networkDagSize: Long, val networkSpeed: Float, val networkSpeedStr: String, val roundShares: String, val avgBlockReward: Float,
                  val avgBlockRewardReal: Float, val coinSymbol: String, val coinAlgorithm: String, val coinReward: Float, val coinPoolFee: Float, val coinBlocktime: Double,
                  val coinConfirmations: Long, val coinUnit: String, val coinExplorer: String, val coinExplorerAddr: String, val coinExplorerBlock: String, val coinExplorerTx: String,
                  val coinMul: Long, val coinMulR: Float, val coinPriceBtc: String, val coinPriceUsd: String, val coinPercentChange: String, val rewardType: String,
                  val dualMining: String)

data class K1PoolMiner(val workersTotal: Long, val workersOnline: Long, val workersOffline: Long, val workers: Map<String, MinerWorker>, val curHashrate: Float,
                       val curHashrateStr: String, val avgHashrate: Float, val avgHashrateStr: String, val coinsPerDay: Double, val totalBlocksFound: Float, val lastShare: Float,
                       val lastShareDiff: Float, val paymentsTotal: Long, val payments: List<MinerPayment>, val immatureBalance: Double, val pendingBalance: Double,
                       val paidBalance: Double, val expectedBalance: Double, val soloLuck: Double, val roundShares: Long, val yourShareRate: Float, val the24HReward: Float,
                       val the24HMevReward: Float, val rewards: List<MinerReward>, val sumrewards: List<MinerSumReward>, val rewardIfFoundNow: Double, val blocks24HReward: Long,
                       val blocksRewards: List<MinerReward>, val blocksSumRewards: List<MinerSumReward>, val blockAvgReward: Double, val minerCharts: List<MinerHashrateChart>)