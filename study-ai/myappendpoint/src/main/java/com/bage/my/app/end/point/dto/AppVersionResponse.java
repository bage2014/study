package com.bage.my.app.end.point.dto;

import lombok.Data;
import com.bage.my.app.end.point.entity.AppVersion;

@Data
public class AppVersionResponse {
    private Boolean needUpdate;
    private String message;
    private AppVersion version;

    public static AppVersionResponse latestVersion(String currentVersion) {
        AppVersionResponse response = new AppVersionResponse();
        response.setNeedUpdate(false);
        response.setMessage("当前已是最新版本");
        return response;
    }

    public static AppVersionResponse updateAvailable(AppVersion version) {
        AppVersionResponse response = new AppVersionResponse();
        response.setNeedUpdate(true);
        response.setMessage("new version available");
        response.setVersion(version);
        return response;
    }
}