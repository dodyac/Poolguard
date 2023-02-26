package com.acxdev.poolguardapps.model.pool_response

data class GeneralInfo(val status: Boolean, val data: GeneralInfoData? = null)

data class GeneralInfoData(val account: String, val unconfirmed_balance: String, val balance: String, val hashrate: String, val avgHashrate: AvgHashrate,
                           val workers: MutableList<Worker>)

data class AvgHashrate(val h1: String, val h3: String, val h6: String, val h12: String, val h24: String)

data class Worker(val id: String, val uid: Long, val hashrate: String, val lastshare: Long, val rating: Long, val h1: String, val h3: String, val h6: String,
                  val h12: String, val h24: String)

data class NanopoolData(val status: Boolean, val data: Double)

data class NanopoolPayments(val status: Boolean, val data: List<NanopoolPaymentsData>)

data class NanopoolPaymentsData(val date: Long, val txHash: String, val amount: Double, val confirmed: Boolean)

data class NanopoolHashRateChart(val status: Boolean, val data: List<NanopoolHashRateChartData>)

data class NanopoolHashRateChartData(val date: Long, val shares: Float, var hashrate: Float, var current: Float, var sma: Float)

data class NanopoolAverageHashRate(val status: Boolean, val data: AvgHashrate)