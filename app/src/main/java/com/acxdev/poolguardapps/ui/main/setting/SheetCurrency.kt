package com.acxdev.poolguardapps.ui.main.setting

import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityCompat
import com.acxdev.commonFunction.adapter.OnClick
import com.acxdev.commonFunction.adapter.OnFilter
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.ext.toasty
import com.acxdev.commonFunction.util.ext.view.setVStack
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.common.base.BaseBottomSheet
import com.acxdev.poolguardapps.databinding.SheetCurrencyBinding
import com.acxdev.poolguardapps.util.listOfCurrency
import java.util.*

class SheetCurrency : BaseBottomSheet<SheetCurrencyBinding>(SheetCurrencyBinding::inflate, isFullScreen = true) {

    private lateinit var adapter: RowCurrency

    override fun SheetCurrencyBinding.configureViews() {
        safeContext {
            sheetToolbar.set(R.string.currency)

            adapter = RowCurrency(listOfCurrency(), object : OnClick<Locale> {
                override fun onItemClick(item: Locale, position: Int) {
                    val currency = Currency.getInstance(item)
                    activity?.let {
                        putPrefs(Constant.SharedPrefs.CURRENCY, currency.currencyCode)
                        toasty(Toast.SUCCESS, R.string.settingSaved)
                        ActivityCompat.recreate(it)
                        dismiss()
                    }
                }
            }, object : OnFilter<Locale> {
                override fun onFilteredResult(list: List<Locale>) {
                    currency.visibleIf(list.isNotEmpty())
                    empty.visibleIf(list.isEmpty())
                }
            })

            currency.setVStack(adapter, hasFixed = true)
//        val currentCurrency = listOfCurrency().find {
//            val currencies = Currency.getInstance(it)
//            currencies.currencyCode == getSavedPrefs(Constant.SharedPrefs.CURRENCY).toString()
//        }
//        lifecycleScope.launch {
//            delay(100)
//            currency.scrollToPosition(listOfCurrency().indexOf(currentCurrency))
//        }

            search.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)

                    emptyTitle.text = getString(R.string.couldFind, s.toString())
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    override fun SheetCurrencyBinding.onClickListener() {

    }
}