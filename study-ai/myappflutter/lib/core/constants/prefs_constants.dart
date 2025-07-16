class PrefsConstants {
  // 用户相关
  static const String userId = 'user_id';
  static const String username = 'username';
  static const String avatarUrl = 'avatar_url';
  static const String token = 'token';
  static const String tokenExpireTime = 'token_expire_time';
  
  // 设置相关
  static const String theme = 'theme';
  static const String language = 'language';
  
  // 其他
  static const String lastLoginTime = 'last_login_time';
  
  // 获取所有key的列表，用于清空
  static List<String> get allKeys => [
    userId,
    username,
    avatarUrl,
    token,
    tokenExpireTime,
    theme,
    language,
    lastLoginTime,
  ];
}