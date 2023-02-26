package com.acxdev.poolguardapps.model.pool_response

data class TwoMinersChart(val actual: List<TwoMinersChartHash>, val apiVersion: Float, val bucket: String)

data class TwoMinersChartHash(val x: Long, val y: Float)