package com.acxdev.poolguardapps.model.pool_response

data class MineXmrBalance(val balance: Double, val thold: Double, val paid: Double, val manp: Double)

data class MineXmrHashrateChart(val time: List<Long>, val hashrate: List<Float>, val hashrateHourAverage: List<Float>, val hashrateInvalid: List<Float>,
                                val hashrateExpired: List<Float>, val workerCount: List<Float>)

data class MineXmrPayments(val total: Long, val page: Long, val pageCount: Long, val payments: List<String>)

data class MineXmrWorkers(val name: String? = null, val hashrate: Float, val hashes: Float, val expired: String? = null, val lastShare: String)