package com.acxdev.poolguardapps.model.pool_response

data class ChartEzil(val time: String, val long_average_hashrate: Float, val short_average_hashrate: Float, val reported_hashrate: Float, val rejected_shares_count: Float,
                     val invalid_shares_count: Float, val stale_shares_count: Float, val accepted_shares_count: Float, val workers_count: Long)

data class CurrentStatsEzil(val eth: StatsEzil, val etc: StatsEzil, val last_share_coin: String, val current_hashrate: Float, val average_hashrate: Float,
                            val last_share_timestamp: Float, val reported_hashrate: Float)

data class StatsEzil(val current_hashrate: Float, val average_hashrate: Float, val last_share_timestamp: Float, val last_share_coin: String, val wallet: String,
                     val reported_hashrate: Float)

data class BalanceEzil(val eth: Double, val etc: Double, val zil: Double, val eth_wallet: String, val zil_wallet: String, val eth_min_payout: Double, val etc_min_payout: Double,
                       val zil_min_payout: Double, val eth_leftovers_withdrawal: Long, val eth_max_gas_price: Double, val created_at: String, val ip_address_hint: String,
                       val promocode_cashback_share: Double, val promocode_expires_at: String, val promocode_code: String)

data class WorkerEzil(val wallet: String, val worker: String, val coin: String, val current_hashrate: Float, val average_hashrate: Float, val last_share_timestamp: Long,
                      val reported_hashrate: Float)

data class PayoutEzil(val eth: List<PayoutEzilData>, val etc: List<PayoutEzilData>, val zil: List<PayoutEzilData>)

data class PayoutEzilData(val amount: Double, val tx_hash: String, val created_at: String, val confirmed_at: String, val coin: String, val confirmed: Boolean,
                          val confirmation_attempts: Long)
