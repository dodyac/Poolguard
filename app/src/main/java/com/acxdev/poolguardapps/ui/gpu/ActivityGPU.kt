package com.acxdev.poolguardapps.ui.gpu

import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.adapter.ViewPager2Adapter
import com.acxdev.commonFunction.util.ext.putExtra
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityGpuBinding

class ActivityGPU : BaseActivity<ActivityGpuBinding>(ActivityGpuBinding::inflate) {

    override fun ActivityGpuBinding.configureViews() {
        val pager2Adapter = ViewPager2Adapter(this@ActivityGPU)
        val mutableListFragment = mutableListOf<Pair<String, Fragment>>()
        mutableListFragment.add(Pair("NVIDIA", FragmentGpu().putExtra("NVIDIA")))
        mutableListFragment.add(Pair("AMD", FragmentGpu().putExtra("AMD")))

        toolbar.set(R.string.myDevice)

        layout.viewPager.adapter = pager2Adapter
        pager2Adapter.setWithTab(mutableListFragment, layout.tabLayout, layout.viewPager)
    }

    override fun ActivityGpuBinding.onClickListener() {

    }
}