package com.acxdev.poolguardapps.model.gpu

import com.acxdev.poolguardapps.repository.MyAlgorithm

data class AlgorithmStats(val algorithm: MyAlgorithm, val hashRate: Double, val power: Double)