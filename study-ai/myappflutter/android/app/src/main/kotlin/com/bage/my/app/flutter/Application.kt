package com.bage.my.app.flutter

import android.app.Application
import com.baidu.lbsapi.SDKInitializer

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化百度地图SDK
        SDKInitializer.initialize(this)
    }
}