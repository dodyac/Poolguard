package com.acxdev.poolguardapps.model.coin

import com.acxdev.poolguardapps.repository.Crypto

data class Coin(val crypto: Crypto, val baseUrl: String)