package com.acxdev.poolguardapps.util

import android.widget.ImageView
import android.widget.TextView
import com.acxdev.commonFunction.util.ext.toPercent
import com.acxdev.commonFunction.util.ext.view.tint
import com.acxdev.commonFunction.util.ext.view.toEditString
import com.acxdev.poolguardapps.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineExceptionHandler

val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

fun initRedGreenIndicator(
    textView: TextView,
    imageView: ImageView,
    percentage: Any,
    isMinus: Boolean
) {
    textView.text = percentage.toPercent()
    if (isMinus) {
        imageView.rotation = 90F
        imageView.tint(imageView.context.getColor(R.color.red))
        textView.setTextColor(textView.context.getColor(R.color.red))
    } else {
        imageView.rotation = 270F
        imageView.tint(imageView.context.getColor(R.color.green))
        textView.setTextColor(textView.context.getColor(R.color.green))
    }
}
fun percentBetween(old: Double, new: Double): Double = (((new - old) * 100) / new)

fun TextInputLayout.isNotEmpty(): Boolean {
    return if(toEditString().isEmpty()) {
        isErrorEnabled = true
        error = "$hint ${context.getString(R.string.cannotEmpty)}"
        requestFocus()
        false
    } else {
        isErrorEnabled = false
        clearFocus()
        true
    }
}