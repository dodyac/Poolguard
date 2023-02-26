package com.acxdev.poolguardapps.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.acxdev.commonFunction.util.ext.view.visibleIf
import com.acxdev.commonFunction.widget.MaxHeightNestedScrollView
import com.acxdev.poolguardapps.R
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation

fun Context.balloonMenu(anchorView: View, isArrowVisible: Boolean = false): Balloon {
    val balloon = Balloon.Builder(this)
        .setLayout(R.layout.layout_menu)
        .setArrowSize(0)
        .setArrowOrientation(ArrowOrientation.TOP)
        .setCornerRadius(7F)
        .setElevation(0)
        .setMarginTop(if(!isArrowVisible) -5 else 7)
        .setMarginRight(if(!isArrowVisible) 3 else 12)
        .setBackgroundColor(Color.TRANSPARENT)
        .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        .build()
    balloon.getContentView().findViewById<MaxHeightNestedScrollView>(R.id.nestedScroll).setMaxHeightDensity(com.acxdev.commonFunction.R.dimen.x200dp)
    balloon.getContentView().findViewById<ImageView>(R.id.arrowMenu).visibleIf(isArrowVisible, true)
    balloon.showAlignBottom(anchorView)
    return balloon
}

fun Context.balloonText(arrowOrientation: ArrowOrientation, text: String): Balloon{
    return Balloon.Builder(this)
        .setArrowSize(10)
        .setArrowPosition(0.5F)
        .setArrowOrientation(arrowOrientation)
        .setMargin(5)
        .setCornerRadius(8F)
        .setElevation(3)
        .setText(text)
        .setTextSize(12F)
        .setTextColor(getColor(R.color.text))
        .setPadding(8)
        .setBackgroundColor(getColor(R.color.background))
        .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        .build()
}