import 'dart:convert';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/core/utils/prefs_util.dart';

class UserUtil {
  // 获取用户名
  static Future<String?> getUserName() async {
    try {
      final userInfo = await PrefsUtil.getString(PrefsConstants.userInfo);
      if (userInfo != null && userInfo.isNotEmpty) {
        final userMap = jsonDecode(userInfo);
        return userMap['username'] as String?;
      }
      return null;
    } catch (e) {
      LogUtil.error('解析用户名出错: \$e');
      return null;
    }
  }

  // 获取用户头像
  static Future<String?> getUserAvatar() async {
    try {
      final userInfo = await PrefsUtil.getString(PrefsConstants.userInfo);
      if (userInfo != null && userInfo.isNotEmpty) {
        final userMap = jsonDecode(userInfo);
        return userMap['avatar'] as String?;
      }
      return null;
    } catch (e) {
      LogUtil.error('解析用户头像出错: \$e');
      return null;
    }
  }
}