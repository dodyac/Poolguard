package com.acxdev.poolguardapps.model.gpu

class GPUDatabase {
    var model: String = ""
    var count: Int = 1
    var isChecked: Int = 0
    var _id: Long? = null

    constructor()

    constructor(
        model: String,
        count: Int,
        isChecked: Int,
        _id: Long? = null
    ) {
        this.model = model
        this.count = count
        this.isChecked = isChecked
        this._id = _id
    }
}