package com.acxdev.poolguardapps.model.pool_response

data class LuckPoolResponse (val timestamp: Long, val address: String, val hashrateSols: Float, val hashrateString: String, val avgHashrateSols: Float, val avgHashrateSols24HR: Float,
                             val currentSharePercent: Float, val estimatedLuck: String, val shares: Float, val efficiency: Float, val currDiff: Long, val peakDiff: Long,
                             val avgShareTime: Double, val stratumServer: String, val immature: Double, val balance: Double, val paid: Double, val workers: List<String>)

data class LuckPoolHashrateResponse (val a: List<Float>)