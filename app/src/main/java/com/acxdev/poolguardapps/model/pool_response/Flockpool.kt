package com.acxdev.poolguardapps.model.pool_response

data class FlockpoolDashboard(val workers: List<FlockpoolWorker>, val balance: FlockpoolBalance, val min_payment: Long, val hashrate_graph: HashrateGraph,
                              val payments: List<FlockpoolPayments?>)

data class FlockpoolBalance(val immature: Double, val mature: Double, val paid: Double)

data class HashrateGraph(val start_time_secs: Long, val hashrate: List<Double>)

data class FlockpoolWorker(val name: String, val shares: Shares, val hashrate: FlockpoolHashrate, val last_seen_secs: Long)

data class FlockpoolHashrate(val now: Float, val avg: Float)

data class Shares(val accepted: Long, val stale: Long, val rejected: Long)

data class FlockpoolPaymentsResponse(val payments: List<FlockpoolPayments>)

data class FlockpoolPayments(val timestamp: Long, val state: Long, val amount: Double, val transaction_id: String?)