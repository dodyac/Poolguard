package com.acxdev.poolguardapps.ui.main.setting

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.showSheetWithExtra
import com.acxdev.commonFunction.util.ext.toasty
import com.acxdev.commonFunction.util.ext.view.setFlex
import com.acxdev.commonFunction.util.ext.view.setHStack
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.whenPermissionGranted
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseFragment
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.databinding.FragmentSettingBinding
import com.acxdev.poolguardapps.model.DefaultScreen
import com.acxdev.poolguardapps.model.Menu
import com.acxdev.poolguardapps.model.SettingTag
import com.acxdev.poolguardapps.model.Settings
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.List.listOfLanguage
import com.acxdev.poolguardapps.repository.List.listOfRefreshPeriod
import com.acxdev.poolguardapps.repository.List.listOfScreen
import com.acxdev.poolguardapps.repository.List.listOfStyle
import com.acxdev.poolguardapps.service.NotificationBroadcast.Companion.startAutoCheck
import com.acxdev.poolguardapps.service.NotificationBroadcast.Companion.stopAutoCheck
import com.acxdev.poolguardapps.ui.about.ActivityAbout
import com.acxdev.poolguardapps.ui.list.ActivityList
import com.acxdev.poolguardapps.ui.main.setting.SettingMenu.appearance
import com.acxdev.poolguardapps.ui.main.setting.SettingMenu.general
import com.acxdev.poolguardapps.ui.main.setting.SettingMenu.joinCommunity
import com.acxdev.poolguardapps.ui.main.setting.SettingMenu.notification
import com.acxdev.poolguardapps.ui.main.setting.SettingMenu.support
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.util.openWebView
import com.acxdev.poolguardapps.util.restartApps
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentSetting : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            safeContext {
                if(result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    //TODO Raw String
                    val value = data?.getStringExtra("value")
//                screen.value.text = value
                    val currentScreen = if(prefs(Constant.SharedPrefs.IS_WALLET_SCREEN).toBoolean()) {
                        val menu = mutableListOf<DefaultScreen>()
                        val walletList = sqLiteZSelectTable(Wallet::class.java)
                        walletList.forEach {
                            menu.add(DefaultScreen(it.name, getString(R.string.wallet), it._id!!.toInt()))
                        }
                        val finder = menu.find { it.value == prefs(Constant.SharedPrefs.SCREEN_DEFAULT).toInt() }?.title
                        finder ?: listOfScreen()[0].title
                    } else {
                        val finder = listOfScreen().find { it.value == prefs(Constant.SharedPrefs.SCREEN_DEFAULT).toInt() }?.title
                        finder ?: listOfScreen()[0].title
                    }

                    general()[2] = Settings(
                        SettingTag.DEFAULT_SCREEN,
                        getString(R.string.defaultScreen),
                        R.drawable.icons8_external_link,
                        com.acxdev.shimmer.R.color.default_color,
                        false,
                        currentScreen
                    )

                    adapterSettingGeneral.updateItem(general())
                    toasty(Toast.SUCCESS, R.string.settingSaved)
                }
            }
        }

    private lateinit var adapterSettingGeneral: RowSetting
    private lateinit var adapterSettingNotification: RowSetting

    private lateinit var bottomSheet: BottomSheetDialogFragment

    override fun FragmentSettingBinding.configureViews() {
        safeContext {
            general.apply {
                title.text = getString(R.string.general)

                adapterSettingGeneral = RowSetting(Type.GENERAL, general(), object : RowSetting.OnBehavior {
                    override fun onItemClick(item: Settings, position: Int) {
                        when(item.tags){
                            SettingTag.CURRENCY -> {
                                showSheetWithExtra(SheetCurrency())
                            }
                            SettingTag.DEFAULT_SCREEN -> {
                                Intent(this@safeContext, ActivityList::class.java)
                                    .putExtra(ConstantLib.DATA, ActivityList.DEFAULT_SCREEN)
                                    .also {
                                        resultLauncher.launch(it)
                                    }
                            }
                            else -> Unit
                        }
                    }

                    override fun onCheckedChange(state: Boolean, item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.DEFAULT_BROWSER -> {
                                putPrefs(Constant.SharedPrefs.IS_OPEN_DEFAULT_BROWSER, state)
                            }
                            else -> Unit
                        }
                    }
                })

                menu.setVStack(adapterSettingGeneral, hasFixed = true)
            }

            appearance.apply {
                title.text = getString(R.string.appearance)

                menu.setVStack(RowSetting(Type.APPEARANCE, appearance(), object : RowSetting.OnBehavior {
                    override fun onItemClick(item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.LANGUAGE -> {
                                bottomSheet = SheetRecycler(R.string.language) { recyclerView, _ ->
                                    recyclerView.textBoxConfiguration(listOfLanguage(), Constant.SharedPrefs.CURRENT_LOCALE) {
                                        bottomSheet.dismiss()
                                        activity?.let {
                                            toasty(Toast.SUCCESS, R.string.settingSaved)
                                            ActivityCompat.recreate(it)
                                        }
                                    }
                                }
                                showSheetWithExtra(bottomSheet)
                            }
                            SettingTag.COLOR -> {
                                bottomSheet = SheetRecycler(R.string.color) { recyclerView, _ ->
                                    recyclerView.setHStack(RowColor(listOfStyle(), object : OnClick<Menu> {
                                        override fun onItemClick(item: Menu, position: Int) {
                                            putPrefs(Constant.SharedPrefs.COLOR, item.title)
                                            bottomSheet.dismiss()
                                            activity?.let {
                                                toasty(Toast.SUCCESS, R.string.settingSaved)
                                                ActivityCompat.recreate(it)
                                            }
                                        }
                                    }), hasFixed = true)

                                    val currentStyle = listOfStyle().find { it.title == getSavedPrefs(Constant.SharedPrefs.COLOR) }
                                    recyclerView.scrollToPosition(listOfStyle().indexOf(currentStyle))
                                }
                                showSheetWithExtra(bottomSheet)
                            }
                            else -> Unit
                        }
                    }

                    override fun onCheckedChange(state: Boolean, item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.THEME -> {
                                activity?.let {
                                    putPrefs(ConstantLib.IS_DARK_MODE, state)
                                    toasty(Toast.SUCCESS, R.string.settingSaved)
                                    ActivityCompat.recreate(it)
                                    if(getSavedPrefs(Constant.SharedPrefs.COLOR) == getString(R.string.midNightAmoled)) {
                                        putPrefs(Constant.SharedPrefs.COLOR, getString(R.string.crayola))
                                    }
                                }
                            }
                            else -> Unit
                        }
                    }

                }), hasFixed = true)
            }

            notification.apply {
                title.text = getString(R.string.notification)

                adapterSettingNotification = RowSetting(Type.NOTIFICATION, notification(), object : RowSetting.OnBehavior {
                    override fun onItemClick(item: Settings, position: Int) {
                        when(item.tags){
                            SettingTag.REPEAT -> {
                                bottomSheet = SheetRecycler(R.string.refreshPeriod) { recyclerView, _ ->
                                    recyclerView.textBoxConfiguration(listOfRefreshPeriod(), Constant.SharedPrefs.REFRESH_PERIOD) {
                                        startAutoCheck()
                                        notification()[1] = Settings(
                                            SettingTag.REPEAT,
                                            getString(R.string.refreshPeriod),
                                            R.drawable.icons8_rollback,
                                            R.color.yellow,
                                            false,
                                            item.title
                                        )
                                        adapterSettingNotification.updateItem(notification())
                                        bottomSheet.dismiss()
                                    }
                                }
                                showSheetWithExtra(bottomSheet)
                            }
                            SettingTag.HASHRATE_DROP -> {
                                showSheetWithExtra(SheetHashRate(object : SheetHashRate.OnSheetHashRate {
                                    override fun onComplete(data: String) {
                                        notification()[3] = Settings(
                                            SettingTag.HASHRATE_DROP,
                                            getString(R.string.hashrateDrop),
                                            R.drawable.icons8_activity,
                                            R.color.redSecondary,
                                            false,
                                            data
                                        )

                                        adapterSettingNotification.updateItem(notification())
                                    }
                                }))
                            }
                            else -> Unit
                        }
                    }

                    override fun onCheckedChange(state: Boolean, item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.ALLOW_NOTIFICATION -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    whenPermissionGranted(Manifest.permission.POST_NOTIFICATIONS) {
                                        putPrefs(Constant.SharedPrefs.AUTO_CHECK, state)
                                        if(state) {
                                            startAutoCheck()
//                                root.snackBarMain(Toast.INFO, getString(R.string.disableBattery))
                                            //TODO NOT READY YET
//                                    root.snackBarDontKill( getString(R.string.disableBattery))
                                        } else {
                                            stopAutoCheck()
                                        }
                                    }
                                } else {
                                    putPrefs(Constant.SharedPrefs.AUTO_CHECK, state)
                                    if(state) {
                                        startAutoCheck()
//                                root.snackBarMain(Toast.INFO, getString(R.string.disableBattery))
                                        //TODO NOT READY YET
//                                    root.snackBarDontKill( getString(R.string.disableBattery))
                                    } else {
                                        stopAutoCheck()
                                    }
                                }
                            }
                            SettingTag.WORKER_OFFLINE -> {
                                putPrefs(Constant.SharedPrefs.WORKER_OFFLINE, state)
                            }
                            SettingTag.PAYMENTS -> {
                                putPrefs(Constant.SharedPrefs.PAYMENT_NOTIFICATION, state)
                            }
                            else -> Unit
                        }
                    }
                })

                menu.setVStack(adapterSettingNotification, hasFixed = true)
            }

            joinCommunity.apply {
                title.text = getString(R.string.joinCommunity)

                menu.setVStack(RowSetting(Type.JOIN_COMMUNITY, joinCommunity(), object : RowSetting.OnBehavior {
                    override fun onItemClick(item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.FACEBOOK -> {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(BaseUrl.Apps.POOLGUARD_FACEBOOK)
                                startActivity(i)
                            }
                            SettingTag.DISCORD -> {
                                Intent(Intent.ACTION_VIEW, Uri.parse(BaseUrl.Apps.POOLGUARD_DISCORD))
                                    .also {
                                        startActivity(it)
                                    }
                            }
                            SettingTag.TELEGRAM -> {

                            }
                            else -> Unit
                        }
                    }

                    override fun onCheckedChange(state: Boolean, item: Settings, position: Int) {

                    }
                }), hasFixed = true)
            }

            support.apply {
                title.text = getString(R.string.support)

                menu.setVStack(RowSetting(Type.SUPPORT, support(), object : RowSetting.OnBehavior {
                    override fun onItemClick(item: Settings, position: Int) {
                        when(item.tags) {
                            SettingTag.SHARE -> {
                                //TODO Raw String
                                ShareCompat.IntentBuilder.from(requireActivity()).setType("text/plain")
                                    .setChooserTitle("Share Via").setText("Download ${getString(R.string.app_name)} today, Get it on " +
                                            "https://poolguard.eagercodes.com/").startChooser()
                            }
                            SettingTag.RATE -> {
                                try {
                                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${packageName}"))
                                        .also {
                                            startActivity(it)
                                        }
                                } catch (e: ActivityNotFoundException) {
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${packageName}"))
                                        .also {
                                            startActivity(it)
                                        }
                                }
                            }
                            SettingTag.HELP_US_TRANSLATE -> {
                                openWebView(BaseUrl.Apps.HELP_TRANSLATE)
                            }
                            SettingTag.WHATS_NEW -> {
                                Intent(this@safeContext, ActivityList::class.java)
                                    .putExtra(ConstantLib.DATA, ActivityList.CHANGELOG)
                                    .also {
                                        startActivity(it)
                                    }
                            }
                            SettingTag.ABOUT -> {
                                Intent(this@safeContext, ActivityAbout::class.java)
                                    .also {
                                        startActivity(it)
                                    }
                            }
                            else -> Unit
                        }
                    }

                    override fun onCheckedChange(state: Boolean, item: Settings, position: Int) {

                    }
                }), hasFixed = true)
            }
        }
    }

    private fun RecyclerView.textBoxConfiguration(list: MutableList<Menu>, prefsPath: String, menu: Menu.() -> Unit) {
        safeContext {
            setFlex(RowTextBoxNew(list, prefsPath, object : OnClick<Menu> {
                override fun onItemClick(item: Menu, position: Int) {
                    toasty(Toast.SUCCESS, R.string.settingSaved)
                    putPrefs(prefsPath, item.value)
                    menu.invoke(item)
//                dismiss()
                }
            }))
        }
    }

    override fun FragmentSettingBinding.onClickListener() {

    }
}