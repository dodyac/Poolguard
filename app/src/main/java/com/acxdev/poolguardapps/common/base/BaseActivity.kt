package com.acxdev.poolguardapps.common.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Inflate
import com.acxdev.commonFunction.common.base.BaseActivityLib
import com.acxdev.commonFunction.util.ext.useCurrentTheme
import com.acxdev.commonFunction.util.ext.view.visible
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.databinding.LayoutToolbarBinding
import com.acxdev.poolguardapps.ui.dialog.DialogProgress
import com.acxdev.poolguardapps.util.currentTheme
import com.acxdev.poolguardapps.util.getSavedPrefs
import java.util.*

abstract class BaseActivity<VB : ViewBinding>(inflate: Inflate<VB>) : BaseActivityLib<VB>(inflate) {

    protected lateinit var dialogProgress: DialogProgress

    override fun onCreate(savedInstanceState: Bundle?) {
        useCurrentTheme()
        setTheme(currentTheme())
        setLocal(prefs(Constant.SharedPrefs.CURRENT_LOCALE))
//        scaleScreen(
//            resources.configuration,
//            Constant.DefaultValue.SIZE_HD,
//            Constant.DefaultValue.SIZE_FHD
//        )
        dialogProgress = DialogProgress()
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = Constant.DefaultValue.FONT_SCALE
        applyOverrideConfiguration(newOverride)
    }

    override fun onResume() {
        useCurrentTheme()
        super.onResume()
    }

    protected fun showDialog() {
        dialogProgress.show(supportFragmentManager, Constant.TAG_DIALOG)
    }

    protected fun dismissDialog() {
        dialogProgress.dismiss()
    }

    fun prefs(path: String): String = getSavedPrefs(path).toString()

    private fun setLocal(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)

        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    protected fun LayoutToolbarBinding.set(@StringRes titles: Int) {
        toolbarTitle.text = toolbarTitle.context.getString(titles)
        toolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    protected fun LayoutToolbarBinding.set(titles: String) {
        toolbarTitle.text = titles
        toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    protected fun LayoutToolbarBinding.setWithMenu(titles: String, onclickMenu: View.() -> Unit) {
        toolbarTitle.text = titles
        toolbarBack.setOnClickListener {
            onBackPressed()
        }
        toolbarMenu.visible()
        toolbarMenu.setOnClickListener {
            onclickMenu.invoke(it)
        }
    }
}