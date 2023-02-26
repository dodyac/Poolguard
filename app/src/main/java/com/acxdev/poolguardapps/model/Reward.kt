package com.acxdev.poolguardapps.model

data class Reward(val price: Double, val secondly: Double, val minutely: Double, val hourly: Double, val daily: Double, val weekly: Double, val monthly: Double,
                  val yearly: Double, val whattomine: Whattomine? = null)