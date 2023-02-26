package com.acxdev.poolguardapps.ui.gpu

import android.text.Editable
import android.text.TextWatcher
import androidx.transition.TransitionManager
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.util.ext.view.gone
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityGpustatsBinding
import com.acxdev.poolguardapps.model.gpu.AlgorithmStats
import com.acxdev.poolguardapps.model.gpu.GPUStats
import com.google.gson.Gson

class ActivityGPUStats : BaseActivity<ActivityGpustatsBinding>(ActivityGpustatsBinding::inflate) {

    override fun ActivityGpustatsBinding.configureViews() {
        val gpu = Gson().fromJson(intent.getStringExtra(Constant.Intent.GPU), GPUStats::class.java)

        toolbar.set(gpu.model)

        val adapter = RowAlgorithm(gpu.algorithm, object : OnFilter<AlgorithmStats> {
            override fun onFilteredResult(list: List<AlgorithmStats>) {
                if (list.isEmpty()) {
                    algorithm.gone()
                    empty.visible()
                } else {
                    TransitionManager.beginDelayedTransition(frame)
                    empty.gone()
                    algorithm.visible()
                }
            }
        })

        algorithm.setVStack(adapter)

        search.gone()

        search.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)

                emptyTitle.text = getString(R.string.couldFind, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun ActivityGpustatsBinding.onClickListener() {

    }
}