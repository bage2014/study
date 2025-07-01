package com.bage.my.app.flutter

import android.app.Application
import com.baidu.mapapi.SDKInitializer

class Application : FlutterApplication() {
    override fun onCreate() {
        super.onCreate()
        // 初始化百度地图SDK
        SDKInitializer.initialize(this)
    }
}