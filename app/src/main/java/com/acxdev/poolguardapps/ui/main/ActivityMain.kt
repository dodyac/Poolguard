package com.acxdev.poolguardapps.ui.main

import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.acxdev.commonFunction.adapter.ViewPager2Adapter
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.toasty
import com.acxdev.poolguardapps.service.NotificationBroadcast.Companion.startAutoCheck
import com.acxdev.poolguardapps.service.NotificationBroadcast.Companion.stopAutoCheck
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityMainBinding
import com.acxdev.poolguardapps.repository.Crypto
import com.acxdev.poolguardapps.ui.main.home.FragmentHome
import com.acxdev.poolguardapps.ui.main.news.FragmentNews
import com.acxdev.poolguardapps.ui.main.profitability.FragmentProfitability
import com.acxdev.poolguardapps.ui.main.setting.FragmentSetting
import com.acxdev.poolguardapps.ui.wallet.FragmentWallet
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityMain : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var doubleBackToExitPressedOnce = false
    private val newsViewModel: NewsViewModel by viewModels()

    override fun ActivityMainBinding.configureViews() {
        val pager2Adapter = ViewPager2Adapter(this@ActivityMain)

        pager2Adapter.sets(
            FragmentHome(),
            FragmentWallet(),
            FragmentNews(),
            FragmentProfitability(),
            FragmentSetting()
        )

        val currentPosition = intent.getIntExtra(
            Constant.Intent.CURRENT_POSITION,
            prefs(Constant.SharedPrefs.SCREEN_DEFAULT).toInt()
        )

        viewPager.adapter = pager2Adapter
        viewPager.offscreenPageLimit = 5

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNav.menu.getItem(position).isChecked = true
                slideUp()
            }
        })

        try {
            val position = if(currentPosition < 5) currentPosition else 0
            viewPager.setCurrentItem(position, false)
            bottomNav.menu.getItem(currentPosition).isChecked = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (prefs(Constant.SharedPrefs.AUTO_CHECK).toBoolean()) {
            startAutoCheck()
        } else {
            stopAutoCheck()
        }

        putPrefs(Constant.SharedPrefs.IS_FIRST_TIME, getString(R.string.no))

        newsViewModel.getNews(Crypto.BTC.name)
    }

    override fun ActivityMainBinding.onClickListener() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.currentItem = 0
                R.id.navigation_wallet -> viewPager.currentItem = 1
                R.id.navigation_news -> viewPager.currentItem = 2
                R.id.navigation_profitability -> viewPager.currentItem = 3
                R.id.navigation_setting -> viewPager.currentItem = 4
            }
            slideUp()
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true

        toasty(Toast.INFO, getString(R.string.tapAgainToExit))

        lifecycleScope.launch(Dispatchers.IO){
            delay(Constant.DefaultValue.DELAY_TAP_TO_EXIT)
            doubleBackToExitPressedOnce = false
        }
    }

    private fun slideUp() {
        scopeLayout {
            val layoutParams = bottomCard.layoutParams as CoordinatorLayout.LayoutParams
            val bottomViewNavigationBehavior = layoutParams.behavior as HideBottomViewOnScrollBehavior
            bottomViewNavigationBehavior.slideUp(bottomCard)
        }
    }
}