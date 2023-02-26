package com.acxdev.poolguardapps.ui.gpu

import android.content.Intent
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.databinding.FragmentGpuBinding
import com.acxdev.poolguardapps.model.gpu.GPUDatabase
import com.acxdev.poolguardapps.repository.GPU.gpuStats
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import com.google.gson.Gson

class FragmentGpu : BaseFragment<FragmentGpuBinding>(FragmentGpuBinding::inflate) {

    override fun FragmentGpuBinding.configureViews() {
        safeContext {
            val db = sqLiteZSelectTable(GPUDatabase::class.java)

            val filter = if(getStringExtra() == "NVIDIA") {
                db.filter { it.model.contains("NVIDIA") }
            } else {
                db.filter { !it.model.contains("NVIDIA") }
            }

            gpu.setVStack(RowGpu(filter, object : RowGpu.OnClick {
                override fun onItemClick(item: GPUDatabase, position: Int) {
                    Intent(this@safeContext, ActivityGPUStats::class.java)
                        .putExtra(Constant.Intent.GPU, Gson().toJson(item.gpuStats()))
                        .also {
                            startActivity(it)
                        }
                }
            }))
        }
    }

    override fun FragmentGpuBinding.onClickListener() {

    }
}