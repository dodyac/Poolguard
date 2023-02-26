package com.acxdev.poolguardapps.model

data class Menu(val title: String, val value: Any)

data class Estimated(val rewardDaily: String?, val localDaily: String?, val hashRate: String?, val rewardMonthly: String?, val localMonthly: String?, val wallet: Wallet)