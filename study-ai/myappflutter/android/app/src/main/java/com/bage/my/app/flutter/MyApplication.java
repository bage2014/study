package com.bage.my.app.flutter;

import com.baidu.mapapi.base.BmfMapApplication;
// import com.baidu.mapapi.common.SDKInitializer;
// import com.baidu.mapapi.map.SDKInitializer;
import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends BmfMapApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        // 同意隐私政策
        SDKInitializer.setAgreePrivacy(this, true);
        // 显式初始化百度地图SDK
        SDKInitializer.initialize(this);
    }
}