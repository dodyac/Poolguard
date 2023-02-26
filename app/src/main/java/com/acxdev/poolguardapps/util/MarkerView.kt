package com.acxdev.poolguardapps.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.acxdev.poolguardapps.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

@SuppressLint("ViewConstructor")
class MarkerView(
    context: Context?,
    layoutResource: Int,
    val list: List<String>,
    val symbol: String
) :
    MarkerView(context, layoutResource) {

    private val date: TextView = findViewById<View>(R.id.date) as TextView
    private val desc: TextView = findViewById<View>(R.id.desc) as TextView

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        if (e is CandleEntry) {
            date.text = list[e.x.toInt()]
            desc.text = Utils.formatNumber(e.high, 0, true)
//            desc.text = e.y.toCryptoCurrency(symbol)
        } else {
            date.text = list[e.x.toInt()]
            desc.text = e.y.toCrypto(symbol, CryptoFormat.FORMAT_SYMBOL)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}