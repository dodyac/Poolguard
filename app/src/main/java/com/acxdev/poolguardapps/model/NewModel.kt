package com.acxdev.poolguardapps.model

import com.acxdev.poolguardapps.common.Changelog

data class PaymentList(val amount: Double, val txHash: String?, val symbol: String, val paidOn: Long, val kernelId: String? = null)

data class WorkerList(val name: String?, val current: Float? = null, val average: Float? = null, val valid: Float? = null, val stale: Float? = null)

data class TimeLine(val icon: Int, val version: String, val date: Long, val field: List<Field>)

data class Field(val title: String, val subtitle: String)

data class WhatsNew(val tag: Changelog, val field: MutableList<Field>)