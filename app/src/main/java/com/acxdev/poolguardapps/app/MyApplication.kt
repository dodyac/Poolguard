package com.acxdev.poolguardapps.app

import android.app.Application
import com.thefinestartist.Base

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Base.initialize(this)
    }
}