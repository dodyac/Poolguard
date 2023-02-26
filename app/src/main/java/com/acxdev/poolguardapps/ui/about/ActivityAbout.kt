package com.acxdev.poolguardapps.ui.about

import android.content.Intent
import android.os.Bundle
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.ext.getVersionName
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.ActivityAboutBinding
import com.acxdev.poolguardapps.ui.list.ActivityList
import com.acxdev.poolguardapps.util.openWebView

class ActivityAbout : BaseActivity<ActivityAboutBinding>(ActivityAboutBinding::inflate) {

    override fun ActivityAboutBinding.configureViews() {
        version.text = "${getString(R.string.version)} ${getVersionName()}"
    }

    override fun ActivityAboutBinding.onClickListener() {
        icon.setOnClickListener {
            openWebView(BaseUrl.URL_ICONS8)
        }

        translator.setOnClickListener {
            Intent(this@ActivityAbout, ActivityList::class.java)
                .putExtra(ConstantLib.DATA, ActivityList.TRANSLATOR)
                .also {
                    startActivity(it)
                }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}