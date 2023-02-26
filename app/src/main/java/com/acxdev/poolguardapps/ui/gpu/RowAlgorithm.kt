package com.acxdev.poolguardapps.ui.gpu

import com.acxdev.commonFunction.adapter.BaseAdapterFilterLib
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.util.ext.view.setText
import com.acxdev.poolguardapps.databinding.RowAlgorithmBinding
import com.acxdev.poolguardapps.model.gpu.AlgorithmStats
import com.acxdev.poolguardapps.util.formatReadable

class RowAlgorithm(list: List<AlgorithmStats>, onFilter: OnFilter<AlgorithmStats>) :
    BaseAdapterFilterLib<RowAlgorithmBinding, AlgorithmStats>(RowAlgorithmBinding::inflate, list, onFilter) {

    override fun ViewHolder<RowAlgorithmBinding>.bind(item: AlgorithmStats) {
        scopeLayout {
            algorithm.text = item.algorithm.name
            hash.suffixText =
                item.algorithm.unit.formatReadable().removePrefix("0 ").removePrefix("1 ")
            power.setText(item.power.toString())
            hash.setText(item.hashRate.toString())
        }

        setIsRecyclable(false)
    }

    override fun filterBy(item: AlgorithmStats, query: String): Boolean {
       return item.algorithm.name.lowercase().contains(query)
    }
}

