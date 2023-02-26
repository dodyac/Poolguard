package com.acxdev.poolguardapps.model.pool_response

data class Stats(val error: Any? = null, val result: Result)

data class Result(val timestamp: Long, val averageEffectiveHashrate: Float, val effectiveHashrate: Float, val currentEffectiveHashrate: Float, val invalidShares: Float,
                  val reportedHashrate: Float, val staleShares: Float, val validShares: Float, val balance: Double, val balanceCountervalue: Float, val price: Double,
                  val workersOffline: Long, val workersOnline: Long, val name: String, val isOnline: Boolean, val count: Long, val lastSeen: Long)

data class Payments(val error: Any? = null, val result: ResultPayments)

data class ResultPayments(val countervalue: Double, val data: List<ResultPaymentsData>, val totalItems: Long, val totalPages: Long)

data class ResultPaymentsData(val hash: String, val timestamp: Long, val value: Double, val fee: Long, val feePercent: Double, val feePrice: Long, val duration: Long,
                              val confirmed: Boolean, val confirmedTimestamp: Long)

data class Chart(val error: String? = null, val result: List<ResultChart>)

data class ResultChart(val timestamp: Long, val effectiveHashrate: Float?, val averageEffectiveHashrate: Float?, val reportedHashrate: Float?, val validShares: Float,
                       val staleShares: Float, val invalidShares: Float)