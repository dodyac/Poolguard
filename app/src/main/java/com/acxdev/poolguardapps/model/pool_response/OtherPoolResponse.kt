package com.acxdev.poolguardapps.model.pool_response

//Etho.club, UPoolIn, HLPool, Tweth
//With Chart MaxHash, MyMiners, SaturnPool, 2Miners, CoMiners, ZetPool, BaikalMine, SoloPool, CrazyPool
data class MinerResponse(val reportedHashrate: Float?, val currentHashrate: Float, val hashrate: Float, val pageSize: Long, val payments: List<MinerPayment>,
                         val paymentsTotal: Long, val roundShares: Float, val stats: MinerStats, val workers: Map<String, MinerWorker>, val workersOffline: Long,
                         val workersOnline: Long, val workersTotal: Long, val the24Hreward: Float, val the24Hnumreward: Float, val rewards: List<MinerReward>,
                         val rewardCharts: List<MinerRewardChart>, val sumrewards: List<MinerSumReward>, val charts: List<MinerHashrateChart>,
                         val minerCharts: List<MinerHashrateChart>, val hashrateHistory: List<MinerHashrateChart>, val shareCharts: List<MinerShareChart>,
                         val paymentCharts: List<MinerPaymentChart>, val nShares: Long, val txCost: Long, val updatedAt: Long, val threshold: Float, val email: String, val ip: String,
    //BaikalMine
                         val A: Float,
                         val I: Float,
                         val S: Float,
    //SoloPool
                         val earnings: List<MinerEarning>,
                         val usageProxy: Boolean,
    //CrazyPool
                         val exchangedata: Map<String, String>,
                         val paymentDaily: List<MinerPayment>,
                         val refList: Any? = null,
                         val shifts: Any? = null,
                         val shiftsToday: List<MinerShift>,
                         val totalSubmitHashrate: Long
)

data class MinerRewardChart(val x: Long, val timeFormat: String, val amount: Double)

data class MinerShareChart(val x: Long, val timeFormat: String, val accepted: Float, val valid: Float, val stale: Float, val invalid: Float, val workerOnline: String)

data class MinerEarning(val period: Long, val amount: Double, val blocks: Long, val luck: Long)

data class MinerShift(val amount: Double, val hashes: Long, val invalid: Float, val lastShift: Long, val shares: Float, val stales: Float, val timestamp: Long, val valid: Float)

data class MinerPaymentChart(val x: Long, val timeFormat: String, val amount: Double)

data class MinerSumReward(val inverval: Long, val reward: Float, val numreward: Long, val upcoming: Double, val name: String, val offset: Float,
    //K1Pool
                          val variance: Double? = null,
                          val invervalfrom: Long,
                          val invervalto: Long,
                          val mevreward: Float? = null
)

data class MinerReward(val blockheight: Float, val timestamp: Long, val blockhash: String, val reward: Float, val percent: Float, val immature: Boolean, val orphan: Boolean,
                       val hashes: Float,
    //SoloPool
    val height: Long,
    val amount: Double,
    val matured: Boolean,
    val luck: Long,
    val shareDifficulty: Long,
    val worker: String,
    val uncle: Boolean,
    //K1Pool
    val currentLuck: Float,
    val mevreward: Float,
    val blockamount: Float,
    val variance: Double,
    val login: String
)

data class MinerWorker(val lastBeat: Float, val hr: Float, val offline: Boolean, val hr2: Float,
    //Cominers
    val valid_shares: Float,
    val stale_shares: Float,
    val invalid_shares: Float,
    val v_per: Float,
    val s_per: Float,
    val i_per: Float,
    val w_stat: Float,
    val w_stat_s: Float,
    //BaikalMine
    val reportedHashrate: Float,
    val A: Float,
    val S: Float,
    val I: Float,
    //SoloPool
    val port: Long,
    //CrazyPool
    val tsubmithashrate: Long,
    val difficulty: Long,
    val hostname: String,
    val sharesLong: Long,
    val sharesShort: Long,
    val submithashrate: Long,
    //K1Pool
    val startedAt: Long,
    val mineZil: Boolean,
    //WoolyPooly
    val worker: String,
    val mode: String,
    val hr3: Float,
    val algo: String,
    val participation: Double,
    val effort: Double
)

data class MinerStats(val balance: Double, val blocksFound: Long, val immature: Double, val lastShare: Long, val paid: Double, val pending: Long,
    //BaikalMine
    val hashCurrent: Float,
    val hashShort: Float,
    val lastPayment: Long,
    val minedCurrent: Float,
    val minedShort: Float,
    //SoloPool
    val roundShares: Long,
    //CrazyPool
    val hopper: Long,
    val invalidCurrent: Float,
    val invalidShort: Long,
    val ip: String,
    val sharesCurrent: Float,
    val sharesShort: Long,
    val stalesCurrent: Float,
    val stalesShort: Long,
    val validCurrent: Float,
    val validShort: Float,
    val fixed: Long,
    val jackpot: Long,
    val preBalance: Long,
    val referral: Long,
    val unclesFound: Long,
)

data class MinerHashrateChart(val x: Long, val timeFormat: String, val minerReportedHash: Float, val minerHash: Float, val minerLargeHash: Float, val workerOnline: String,
                              val reportedHash: Float,
    //ZetPool
    val hr: Float,
    val time: Long,
    val label: String,
    //SoloPool
    val y: Float,
)

data class MinerPayment(val amount: Double, val timestamp: Long, val tx: String,
    //for SaturnPool
    val Address: String,
    @JvmField
    val Amount: Double,
    @JvmField
    val Timestamp: Long,
    @JvmField
    val Tx: String,
    //CrazyPool
    val timeFormat: String,
    val amountPay: Long,
    val bonusTotal: Long,
    val feeTotal: Long,
    val jackpot: Long,
    val referral: Long,
    //K1Pool
    val expected: Double,
)