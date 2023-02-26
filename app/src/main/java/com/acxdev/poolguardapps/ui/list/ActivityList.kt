package com.acxdev.poolguardapps.ui.list

import android.content.Intent
import androidx.core.view.setPadding
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseActivity
import com.acxdev.poolguardapps.databinding.ActivityListBinding
import com.acxdev.poolguardapps.model.DefaultScreen
import com.acxdev.poolguardapps.model.TimeLine
import com.acxdev.poolguardapps.model.Translator
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.ChangelogNote
import com.acxdev.poolguardapps.repository.List.listOfScreen
import com.acxdev.poolguardapps.util.pool
import com.acxdev.sqlitez.SqliteZ.Companion.sqLiteZSelectTable

class ActivityList : BaseActivity<ActivityListBinding>(ActivityListBinding::inflate) {

    companion object {
        const val DEFAULT_SCREEN = "defaultScreen"
        const val CHANGELOG = "changeLog"
        const val TRANSLATOR = "translator"
    }

    override fun ActivityListBinding.configureViews() {
        when(intent.getStringExtra(ConstantLib.DATA)) {
            DEFAULT_SCREEN -> {
                toolbar.set(R.string.defaultScreen)

                val menu = listOfScreen()

                val walletList = sqLiteZSelectTable(Wallet::class.java)
                walletList.forEach {
                    menu.add(DefaultScreen(it.name, "${getString(R.string.wallet)}: ${it.pool().value}", it._id!!.toInt()))
                }

                recycler.setVStack(RowDefaultScreen(menu, object : OnClick<DefaultScreen> {
                    override fun onItemClick(item: DefaultScreen, position: Int) {
                        putPrefs(Constant.SharedPrefs.SCREEN_DEFAULT, item.value)
                        putPrefs(Constant.SharedPrefs.IS_WALLET_SCREEN, position > 4)
                        val resultIntent = Intent()
                        //TODO Raw String
                        resultIntent.putExtra("value", item.title)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }), hasFixed = true)
            }
            CHANGELOG -> {
                toolbar.set(R.string.changelog)

                val timeLine = mutableListOf<TimeLine>()

                recycler.setPadding(resources.getDimensionPixelSize(com.acxdev.commonFunction.R.dimen.x16dp))
                recycler.setVStack(RowTimeLine(timeLine.asReversed()),hasFixed = true)

                timeLine.add(TimeLine(R.drawable.ic_ok,"1.0",1625614058, ChangelogNote.v1()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.01",1625665194, ChangelogNote.v1point01()))
                timeLine.add(TimeLine(R.drawable.ic_fix,"1.02",1625754270, ChangelogNote.v1point02()))
                timeLine.add(TimeLine(R.drawable.ic_improvements,"1.03",1626718234, ChangelogNote.v1point03()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.04",1627958111, ChangelogNote.v1point04()))
                timeLine.add(TimeLine(R.drawable.ic_fix,"1.05",1628243725, ChangelogNote.v1point05()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.06",1628531966, ChangelogNote.v1point06()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.07",1628914085, ChangelogNote.v1point07()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.08",1629369486, ChangelogNote.v1point08()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.09",1630756834, ChangelogNote.v1point09()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.10",1631268021, ChangelogNote.v1point10()))
                timeLine.add(TimeLine(R.drawable.ic_improvements,"1.11",1631455266, ChangelogNote.v1point11()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.12",1631948787, ChangelogNote.v1point12()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.13",1632725089, ChangelogNote.v1point13()))
                timeLine.add(TimeLine(R.drawable.ic_fix,"1.14",1633137521, ChangelogNote.v1point14()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.15",1636984089, ChangelogNote.v1point15()))
                timeLine.add(TimeLine(R.drawable.ic_fix,"1.16",1637848140, ChangelogNote.v1point16()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.17",1643556677, ChangelogNote.v1point17()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.18",1646097072, ChangelogNote.v1point18()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.19",1650280804, ChangelogNote.v1point19()))
                timeLine.add(TimeLine(R.drawable.ic_add,"1.20",0, ChangelogNote.v1point20()))
            }
            TRANSLATOR -> {
                toolbar.set(R.string.translator)

                //TODO Raw String
                val translator = listOf(
                    Translator(
                        getString(R.string.spanish),
                        "Maximo Obetko",
                        "1.15"
                    ),
                    Translator(
                        getString(R.string.russian),
                        "Алексей",
                        "1.15"
                    ),
                    Translator(
                        getString(R.string.chinese),
                        "po1son",
                        "1.15"
                    ),
                    Translator(
                        getString(R.string.bahasa),
                        "Acx",
                        "1.19"
                    )
                )

                recycler.setVStack(RowTranslator(translator.toMutableList()), hasFixed = true)
            }
        }
    }

    override fun ActivityListBinding.onClickListener() {

    }
}