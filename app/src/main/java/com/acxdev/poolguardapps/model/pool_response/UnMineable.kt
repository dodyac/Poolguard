package com.acxdev.poolguardapps.model.pool_response

data class UnMineablePayments(val success: Boolean, val message: String, val data: UnMineablePaymentsData)

data class UnMineablePaymentsData(val explorer: String, val coin: String, val list: List<UnMineablePaymentList>, val hasNext: Boolean, val hasPrev: Boolean, val lastPage: Long)

data class UnMineablePaymentList(val tx: String, val amount: Double, val timestamp: Long, val status: String, val id: Long, val network: String, val explorer: String)

data class UnMineableStats(val success: Boolean, val msg: String, val data: DataUnMineable)

data class DataUnMineable(val balance: String, val balance_payable: String, val precision: String, val payment_threshold: String, val mining_fee: String, val auto: Boolean,
                          val enabled: Boolean, val enabled_auto_only: Boolean, val uuid: String, val fresh: Boolean, val address: String, val network: String,
                          val err_flags: ErrFlags, val chains: List<Chain>)

data class Chain(val name: String, val network: String, val token_standard: String, val regex: String, val regex_memo: Any? = null, val is_default: Boolean,
                 val explorer_address: String, val enabled: Boolean, val enabled_auto_only: Boolean, val payment_threshold: String, val compatible: Boolean)

data class ErrFlags(val payment_error: Boolean, val missing_memo: Boolean, val not_activated: Boolean, val restricted: Boolean)

data class UnMineableWorker (val success: Boolean, val msg: String, val data: UnMineableWorkerData)

data class UnMineableWorkerData (val ethash: Etchash, val etchash: Etchash, val randomx: Etchash, val kawpow: Etchash)

data class Etchash (val workers: List<WorkerRes>, val chart: UnMineableChart)

data class UnMineableChart (val reported: Ted, val calculated: Ted)

data class Ted (val data: List<Float>, val timestamps: List<Long>)

data class WorkerRes (val online: Boolean, val name: String, val last: Long, val rhr: Float, val chr: Float, val referral: Any? = null)