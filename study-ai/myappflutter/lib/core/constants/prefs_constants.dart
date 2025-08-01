class PrefsConstants {
  // 用户相关
  static const String userId = 'user_id';
  static const String userInfo = 'user_info';
  static const String avatarUrl = 'avatar_url';
  static const String token = 'token';
  static const String refreshToken = 'refresh_token';
  static const String tokenExpireTime = 'token_expire_time';

  // 设置相关
  static const String theme = 'theme';
  static const String language = 'language';
  static const String env = 'env';

  // 其他
  static const String lastLoginTime = 'last_login_time';

  // 获取所有key的列表，用于清空
  static List<String> get allKeys => [
    userId,
    userInfo,
    avatarUrl,
    token,
    refreshToken,
    tokenExpireTime,
    theme,
    language,
    env,
    lastLoginTime,
  ];
}
