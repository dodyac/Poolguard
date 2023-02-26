package com.acxdev.poolguardapps.model

class Notification {
    lateinit var tx: String
    var _id: Long? = null

    constructor()

    constructor(
        tx: String,
        _id: Long? = null
    ) {
        this.tx = tx
        this._id = _id
    }
}