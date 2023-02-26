package com.acxdev.poolguardapps.model

class Wallet {
    lateinit var pool: String
    lateinit var name: String
    lateinit var address: String
    lateinit var symbol: String
    var _id: Long? = null

    constructor()

    constructor(
        pool: String,
        name: String,
        address: String,
        symbol: String,
        _id: Long? = null
    ) {
        this.pool = pool
        this.name = name
        this.address = address
        this.symbol = symbol
        this._id = _id
    }
}