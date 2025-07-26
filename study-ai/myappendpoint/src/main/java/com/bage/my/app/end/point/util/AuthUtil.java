package com.bage.my.app.end.point.util;

import com.bage.my.app.end.point.dto.UserContext;

public class AuthUtil {
    /**
     * 获取当前登录用户的ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        return UserContext.getUserId();
    }

    /**
     * 检查用户是否已登录
     * @return 是否登录
     */
    public static boolean isLoggedIn() {
        return UserContext.getUserId() != null;
    }
}