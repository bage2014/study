package com.bage.my.app.flutter;

import com.baidu.mapapi.base.BmfMapApplication;
// import com.baidu.mapapi.common.SDKInitializer;

public class MyApplication extends BmfMapApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        // 显式初始化百度地图SDK
        // SDKInitializer.initialize(this);
    }
}