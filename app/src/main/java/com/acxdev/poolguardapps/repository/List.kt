package com.acxdev.poolguardapps.repository

import android.content.Context
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.model.DefaultScreen
import com.acxdev.poolguardapps.model.Menu

object List {

    fun Context.sortByTime(): MutableList<Menu>{
        val timeIn = mutableListOf<Menu>()
        timeIn.add(Menu(getString(R.string.oneDay), emptyString()))
        timeIn.add(Menu(getString(R.string.oneHour), emptyString()))
        timeIn.add(Menu(getString(R.string.hours, 2), emptyString()))
        timeIn.add(Menu(getString(R.string.hours, 5), emptyString()))
        timeIn.add(Menu(getString(R.string.hours, 10), emptyString()))
        return timeIn
    }

    fun Context.listOfLanguage(): MutableList<Menu>{
        val list = mutableListOf<Menu>()
        list.add(Menu(getString(R.string.english), "en"))
        list.add(Menu(getString(R.string.spanish), "es"))
        list.add(Menu(getString(R.string.russian), "ru"))
        list.add(Menu(getString(R.string.chinese), "zh"))
        list.add(Menu(getString(R.string.bahasa), "in"))
        return list
    }

    fun Context.listOfStyle(): MutableList<Menu>{
        val list = mutableListOf<Menu>()
        list.add(Menu(getString(R.string.crayola), R.color.primaryBlue))
        if(getPrefs().getBoolean(ConstantLib.IS_DARK_MODE, false)) {
            list.add(Menu(getString(R.string.midNightAmoled), R.color.primaryBlue))
        }
        list.add(Menu(getString(R.string.electricPink), R.color.red))
        list.add(Menu(getString(R.string.limeGreen), R.color.green))
        list.add(Menu(getString(R.string.vividGamboge), R.color.yellow))
        list.add(Menu(getString(R.string.blueCola), R.color.blue2))
        list.add(Menu(getString(R.string.ufoGreen), R.color.green2))
        list.add(Menu(getString(R.string.bittersweet), R.color.red2))
        list.add(Menu(getString(R.string.spanishYellow), R.color.yellow2))
        list.add(Menu(getString(R.string.iris), R.color.purple))
        return list
    }

    fun Context.listOfRefreshPeriod(): MutableList<Menu>{
        val list = mutableListOf<Menu>()
        list.add(Menu(getString(R.string.minutes, 5),300000L))
        list.add(Menu(getString(R.string.minutes, 10),600000L))
        list.add(Menu(getString(R.string.minutes, 15),900000L))
        list.add(Menu(getString(R.string.minutes, 30),1800000L))
        list.add(Menu(getString(R.string.oneHour),3600000L))
        list.add(Menu(getString(R.string.hours, 2),7200000L))
        list.add(Menu(getString(R.string.hours, 5),18000000L))
        list.add(Menu(getString(R.string.hours, 10),36000000L))
        list.add(Menu(getString(R.string.oneDay),86400000L))
        return list
    }

    fun Context.listOfScreen(): MutableList<DefaultScreen>{
        val list = mutableListOf<DefaultScreen>()
        list.add(DefaultScreen(getString(R.string.home), getString(R.string.app_name), 0))
        list.add(DefaultScreen(getString(R.string.wallets), getString(R.string.app_name), 1))
        list.add(DefaultScreen(getString(R.string.news), getString(R.string.app_name), 2))
        list.add(DefaultScreen(getString(R.string.calculator), getString(R.string.app_name), 3))
        list.add(DefaultScreen(getString(R.string.settings), getString(R.string.app_name), 4))
        return list
    }
}