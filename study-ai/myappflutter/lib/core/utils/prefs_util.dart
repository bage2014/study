import 'package:shared_preferences/shared_preferences.dart';

import '../constants/prefs_constants.dart';

class PrefsUtil {
  // 清空所有SharedPreferences
  static Future<void> clearAll() async {
    for (final key in PrefsConstants.allKeys) {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove(key);
    }
  }

  static Future<void> remove(String key) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(key);
  }

  // 获取SharedPreferences中的字符串值
  static Future<String?> getString(String key) async {
    final _prefs = await SharedPreferences.getInstance();
    return _prefs.getString(key);
  }

  // 设置SharedPreferences中的字符串值
  static Future<void> setString(String key, String value) async {
    final _prefs = await SharedPreferences.getInstance();
    await _prefs.setString(key, value);
  }

  // 其他SharedPreferences相关方法可以继续添加...
}
