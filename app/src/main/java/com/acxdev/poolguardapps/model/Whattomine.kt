package com.acxdev.poolguardapps.model

data class Whattomine(val id: Long, val name: String, val tag: String, val algorithm: String, val block_time: Double, val block_reward: Double, val block_reward24: Double,
                      val block_reward3: Double, val block_reward7: Double, val last_block: Double, val difficulty: Double, val difficulty24: Double, val difficulty3: Double,
                      val difficulty7: Double, val nethash: Double, val exchange_rate: Double, val exchange_rate24: Double, val exchange_rate3: Double, val exchange_rate7: Double,
                      val exchange_rate_vol: Double, val exchange_rate_curr: String, val market_cap: String, val pool_fee: String, val estimated_rewards: String,
                      val btc_revenue: String, val revenue: String, val cost: String, val profit: String, val status: String, val lagging: Boolean, val testing: Boolean,
                      val listed: Boolean, val timestamp: Long)